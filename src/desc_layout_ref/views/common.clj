(ns desc_layout_ref.views.common
  (:require [desc_layout_ref.sforce :as sforce]
            [noir.session :as session])
  (:use noir.core
        [hiccup.form :only [form-to submit-button]]
        hiccup.page
        hiccup.core
        hiccup.element
        desc_layout_ref.views.layout
        ))

(def USERNAME "emoses-ee@emoses-ws.org")
(def PASSWORD "123456")

(defpartial layout [& content]
            (html5
              [:head
               [:title "Describe Layout Reference"]
               (include-css "/css/reset.css")
               (include-css "/css/layout.css")]
              [:body
               [:div#wrapper
                content]]))

(defn layoutableObjects [con]
  (let [result (sforce/describeGlobal con)]
    (map (fn [sObj] {:name (.getName sObj) :label (.getLabel sObj)})
         (filter (fn [obj] (. obj getLayoutable)) (.getSobjects result)))))

(defpartial layoutableObject [{:keys [label name]}]
  (link-to (str "/c/describeLayout/" name) label))

(defpage [:get "/login"] {}
  (layout
   (form-to [:post "/login"]
            (submit-button "Login"))))

(defpage [:post "/login"] {}
  (if-let [con (sforce/login USERNAME PASSWORD)]
    (do
      (session/put! :connection con)
      (layout
       [:h1 "Success"]
       (unordered-list
         (map layoutableObject (layoutableObjects con)))
        ))
    (layout "Failure")))


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
    