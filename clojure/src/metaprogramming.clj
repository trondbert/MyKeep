(defmacro unless
  "Similar to if but negates the condition"
  [condition & forms]
  `(if (not ~condition)
     ~@forms))

(defn pln
  [function & args]
  (println (apply function args)))

(unless (= 1 2)
        (println "Clojure might still be working")
        (println "Clojure stopped working"))

(println (read-string "(println \"foo\")"))

(pln macroexpand-1 '(unless (= 1 2) true false))

(defmacro unsplice
  [& coll]
  `(do ~@coll))
;= #'user/unsplice

(pln macroexpand-1 '(unsplice (def a 1) (def b 2)))
;= (do (def a 1) (def b 2))

(unsplice (def a 1) (def b 2))
;= #'user/b

(def foo 1)

(let [foo "rew"]
  (println foo))

