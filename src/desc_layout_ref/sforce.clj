(ns desc_layout_ref.sforce
  (:import [com.sforce.ws ConnectorConfig]
           [com.sforce.soap.partner PartnerConnection]))

(def AUTH-ENDPOINT "http://localhost:8080/services/Soap/u/26.0")
(comment (def TOKEN "TWtu1H5iW3h9o0Crz7PFdoRNn"))

(defn login [username pass key]
  (let [config (ConnectorConfig.)]
    (doto config
      (.setUsername username)
      (.setPassword (str pass key))
      (.setAuthEndpoint AUTH-ENDPOINT))
    (try 
      (PartnerConnection. config)
      (catch Exception e
        (.printStackTrace e)))))

(defn describeGlobal [connection]
  (.describeGlobal connection))

(defn describeLayout [connection type recordTypes]
  (.describeLayout connection type (into-array String recordTypes)))

(defn logout [connection]
  (.logout connection))

    
    
  
      