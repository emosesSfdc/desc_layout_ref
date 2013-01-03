(ns desc_layout_ref.server
  (:require [noir.server :as server]
            [noir.session :as session]
            [noir.response :as resp]
            [desc_layout_ref.sforce :as sforce])
  (:use [hiccup.page :only [html5]]
        [hiccup.form :only [form-to submit-button]]
        [noir.core :only [pre-route defpage]]))

(server/load-views-ns 'desc_layout_ref.views)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8085"))]
    (server/start port {:mode mode
                        :ns 'desc_layout_ref})))

(pre-route "/c/*" {}
           (when-not (session/get :connection)
             (resp/redirect "/login")))

(defpage [:get "/login"] {}
  (html5
   (form-to [:post "/login"]
            (submit-button "Login"))))

(defpage [:post "/login"] {}
  (if-let [con (sforce/login "emoses-ee@emoses-ws.org" "123456")]
    (do
      (session/put! :connection con)
      (html5
       "Success"))
    (html5 "Failure")))

  
    
