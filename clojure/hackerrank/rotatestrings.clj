
(defn rotate [string positions]
    (clojure.string/join ""
        (concat 
            (take-last (- (count string) positions) string)
            (take positions string))))

(read-line)
(def line (read-line))
(while (not (nil? line))
    (println (clojure.string/join " "
        (map rotate
                (repeat line)
                (range 1 (+ 1 (count line))))
    ))

    (def line (read-line))
)
