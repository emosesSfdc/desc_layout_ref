(ns desc_layout_ref.views.layout
  (:use noir.core
        hiccup.page
        hiccup.core
        hiccup.element))


(defpartial component [component]
  (html5
   [:ul
    [:li (str "Type: " (.getType component))]
    [:li (str "Value: " (.getValue component))]
    (let [lines (.getDisplayLines component)]
      (if (> 0 lines) [:li (str "Display Lines: " lines)]))
    [:li (str "Tab Order: " (.getTabOrder component))]]))

(defpartial editable [editable?]
  (if editable? "Editable" "Read-only"))

(defpartial required []
  "Required")

(defpartial layoutItem [item]
  (html5
   [:div.item
    [:span.label (.getLabel item)]
    [:span.value
     (if (.getPlaceholder item) "Blank ")
     (editable (.getEditable item))
     (if (.getRequired item) (required))
     " "
     (map component (.getLayoutComponents item))]]))

(defpartial layoutRow [row]
  (html5
   [:div.layoutRow
    (map layoutItem (.getLayoutItems row))]))

(defpartial listInfo [label value]
  (html5
   [:span.infoItem label ": " value]))

(defpartial rlColumn [column]
  (html5
   [:div.column
    [:span.label (.getLabel column)]
    [:span.name "[" (.getField column) " - " (.getName column) "]"]
    (if-not (empty? (.getFormat column)) [:span.info "Format: " (.getFormat column)])]))

(defpartial relatedList [list]
  (html5
   [:h3.label (.getLabel list) [:span.name (str " [" (.getName list) "]")]]
   [:div.info
    (listInfo "Field" (.getField list))
    (listInfo "Object" (.getSobject list))
    [:span.infoItem (if (.getCustom list) "Custom" "Standard")]
    (listInfo "Row Limit" (.getLimitRows list))
    (if (> 0 (count (.getSort list)))
      (listInfo "Sort" (apply str (interpose ","
                                             (map
                                              (fn [sort] (str (.getColumn sort) " " (if (.getAscending sort)
                                                                                      "ASC"
                                                                                      "DESC")))
                                              (.getSort list))))))]
   [:div.columns (map rlColumn (.getColumns list))]))
    

(defpartial relatedLists [lists]
  (map relatedList lists))


(defpartial layoutSection [section]
  (html5
   [:h3 (.getHeading section)
    [:span.columns (str " - " (.getColumns section) " Column")]             
    (if-not (.getUseHeading section) " - (hidden)")
    (if (.getUseCollapsibleSection section) " - collapsible")]
   [:div.layoutRows
    (map layoutRow (.getLayoutRows section))]))

(defpartial layoutSections [sections]
  (map layoutSection sections))
   
(defpartial buttonSection [buttons]
  (if (> 0 (count buttons))
    (unordered-list
     (map (fn [button]
            (html5 [:div.button
                    [:div.label (str (.getLabel button) " - " (if (.isCustom button) "Cust" "Std"))]
                    [:div.name (.getName button)]]))
          (.getDetailButtons buttons)))))

