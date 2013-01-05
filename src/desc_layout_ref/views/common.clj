(ns desc_layout_ref.views.common
  (:require [desc_layout_ref.sforce :as sforce]
            [noir.session :as session])
  (:use noir.core
        [hiccup.form :only [form-to submit-button label password-field text-field]]
        hiccup.page
        hiccup.core
        hiccup.element
        desc_layout_ref.views.layout
        ))


(def USERNAME "emoses-ee@emoses-ws.org")
(def PASSWORD "123456")

(defn session []
  (session/get :connection))

(defpartial layout [& content]
            (html5
              [:head
               [:title "Describe Layout Reference"]
               (include-css "/css/reset.css")
               (include-css "/css/layout.css")]
              [:body
               [:div#wrapper
                content]]))

(defpage "/home" {}
  (layout
   [:h1 "Welcome"]
   [:p "This is a the DescribeLayout browser."]
   (if-let [con (session/get :connection)]
     (html5
      [:p "You are logged in as " (.. con (.getUserInfo) (.getUserName))]
      [:div.links (link-to "/c/listObjects" "Object List")])
     (html5
      [:div.links (link-to "/login" "Log In")]))))
  

(defn layoutableObjects [con]
  (let [result (sforce/describeGlobal con)]
    (map (fn [sObj] {:name (.getName sObj) :label (.getLabel sObj)})
         (filter (fn [obj] (. obj getLayoutable)) (.getSobjects result)))))

(defpartial layoutableObject [{:keys [label name]}]
  (link-to (str "/c/describeLayout/" name) label))

(defpartial objectsList []
  (let [con (session)]
    (unordered-list
     (map layoutableObject (layoutableObjects con)))
    ))

(defpartial login-form []
   (form-to [:post "/login"]
            (label "username" "Username: ")
            (text-field "username" nil)
            (label "password" "Password: ")
            (password-field "password" nil)
            (label "key" "Security Key: ")
            (text-field "key" nil)
            (submit-button "Login")))

(defpage "/c/objectList" {}
  (layout (objectsList)))

(defpage [:get "/login"] {}
  (layout (login-form)))

(defpage [:post "/login"] {:as userinfo}
  (if-let [con (sforce/login (:username userinfo) (:password userinfo) (:key userinfo))]
    (do
      (session/put! :connection con)
      (layout
       [:h1 "Success"]
       (link-to "/c/objectList" "Object List")))
      
    (layout
     [:h1 "Failure"]
     (login-form))))


(defpartial printLayout [layout]
  (html5
   [:h1 (str "Layout ID: " (.getId layout))]
   [:div.section
    [:h2 "Button Section"]
    [:div.buttons (buttonSection (.getButtonLayoutSection layout))]]
   [:div.section
    [:h2 "Detail Layout"]
    [:div.layout (layoutSections (.getDetailLayoutSections layout))]]
   [:div.section
    [:h2 "Edit Layout"]
    [:div.layout (layoutSections (.getEditLayoutSections layout))]]
   [:div.section
    [:h2 "Related Lists"]
    [:div.relatedLists (relatedLists (.getRelatedLists layout))]]))




(defpage [:get "/c/describeLayout/:objectType"] {:keys [objectType rtId]}
  (let [con (session/get :connection)]
    (if-let [result (sforce/describeLayout con objectType (or rtId []))]
      (layout
       [:h1 (str "Layout(s) for " objectType)]
       (map printLayout (.getLayouts result)))
      (layout "Failure"))))
    