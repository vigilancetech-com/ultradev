(set-env!
 :dependencies '[;; repl stuff here
                  [adzerk/boot-cljs-repl   "0.4.0"] ;; latest release
                  [cider/piggieback        "LATEST"  :scope "test"]
                  [weasel                  "0.7.0"  :scope "test"]
                  [nrepl                   "0.6.0"  :scope "test"]
                  [cider/cider-nrepl "0.25.0-alpha1"]
                  [refactor-nrepl "2.5.0"]
                 ;; regular stuff
                  [adzerk/boot-cljs          "LATEST"]
                  [adzerk/boot-reload        "LATEST"]
                  [hoplon/hoplon             "6.0.0-alpha17"]
                  [org.clojure/clojure       "LATEST"]
                  [org.clojure/clojurescript "1.9.293"]
                  [tailrecursion/boot-jetty  "LATEST"]]
  :source-paths #{"src"}
  :asset-paths  #{"assets"})

(require
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl cljs-repl-env]]
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[tailrecursion.boot-jetty :refer [serve]]
  '[boot.repl])

;; (require 'boot.repl)

(swap! boot.repl/*default-dependencies*
       concat '[[cider/cider-nrepl "0.25.0-alpha1"]
                [refactor-nrepl "2.5.0"]])

(swap! boot.repl/*default-middleware*
       concat '[cider.nrepl/cider-middleware
                refactor-nrepl.middleware/wrap-refactor])

(deftask dev
  "Build ultradev for local development."
  []
  (comp
    (watch)
    (speak)
    (hoplon)
    (reload)
;;    (cljs-repl-env)
    (cljs-repl)
    (cljs)
    (serve :port 8000)))

(deftask prod
  "Build ultradev for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (target :dir #{"target"})))
