(ns fetch.app 
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [reagent.core :as r]
            [reagent.dom :as rdom]))

(defonce users (r/atom []))

(defn some-component []
  [:div
   [:h3 "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red"]
    " text."]
   [:ul
    (for [user @users]
      ^{:key user}
      [:li (str (user :login))])]
   ])

(defn ^:export render-component []
    (rdom/render [some-component] (js/document.getElementById "root")))

(defn init []
  (println "Hello World")
  (go (let [response (<! (http/get "https://api.github.com/users"
                                   {:with-credentials? false
                                    :query-params {"since" 135}}))]
        (prn (:status response))
        (prn (:body response))
        (reset! users (:body response))
       ;; (prn map :login (:body response))
        ))
  (render-component))
(comment
(print @users)  
  )
