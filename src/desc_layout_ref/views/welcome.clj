(ns desc_layout_ref.views.welcome
  (:require [desc_layout_ref.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to desc_layout_ref"]))
