(ns desc_layout_ref.server
  (:require [noir.server :as server]
            [noir.session :as session]
            [noir.response :as resp])
  (:use [noir.core :only [pre-route]]))

(server/load-views-ns 'desc_layout_ref.views)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8085"))]
    (server/start port {:mode mode
                        :ns 'desc_layout_ref})))

(pre-route "/c/*" {}
           (when-not (session/get :connection)
             (resp/redirect "/login")))

   



  
    

