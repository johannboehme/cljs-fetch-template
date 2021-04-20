(ns fetch.app 
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as r]
            [reagent.dom :as rdom]))

(defonce users (r/atom []))

(defn load-users []
  (go (let [response (<! (http/get "https://api.github.com/users"
                                   {:with-credentials? false
                                    :query-params {"since" 135}}))]
        (prn (:status response))
        (prn (:body response))
        (reset! users (:body response))
       ;; (prn map :login (:body response))
        )))

(defn user-component []
  [:div
   [:h3 "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red"]
    " text."]
   [:ul {:style {:list-style-type "none"}}
    (for [user @users]
      ^{:key user}
      [:li
       [:img {:src (user :avatar_url) :style {:margin "0 10px"  :width 50 :height 50 :border-radius 4}}]
       (str (user :login))])]])

(defn ^:export init []
  (load-users)
  (rdom/render [user-component] (js/document.getElementById "root")))


(comment
(print @users)  
  )
