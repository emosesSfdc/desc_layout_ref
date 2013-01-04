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
     (if (.getPlaceholder item) "Blank")
     (editable (.getEditable item))
     (if (.getRequired item) (required))
     " "
     (map component (.getLayoutComponents item))]]))

(defpartial layoutRow [row]
  (html5
   [:div.layoutRow
    (map layoutItem (.getLayoutItems row))]))

(defpartial relatedLists [lists]
  (str (count lists) " related list(s)"))


(defpartial layoutSection [section]
  (html5
   [:h3 (str (.getHeading section)
             (if-not (.getUseHeading section) " (hidden)")
             (if (.getUseCollapsibleSection section) " - collapsible"))]
   [:div.layoutRows
    (map layoutRow (.getLayoutRows section))]))

(defpartial layoutSections [sections]
  (map layoutSection sections))
   
(defpartial buttonSection [buttons]
  (unordered-list
   (map (fn [button]
          (html5 [:div.button
                  [:div.label (str (.getLabel button) " - " (if (.isCustom button) "Cust" "Std"))]
                  [:div.name (.getName button)]]))
           (.getDetailButtons buttons))))