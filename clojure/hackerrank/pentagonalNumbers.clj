(defn unique [n]
    (- (* 3 n) 2)
)

(defn pentaSlow [n]
    (if (= n 1)
        1
        (+ (unique n) (pentaSlow (- n 1)))
    )
)
(defn pentaFast [n]
    (+ 1
    (/ 
        (- (* 3 (* n n)) n 2)
        2
    ))
)

(def lines (map read-string (line-seq (java.io.BufferedReader. *in*))))

(doall (map println
    (map pentaFast (rest lines))
))

;;
;;Shared
;;2*(1...n-1) - (n-1)
;;
;;2*(n-1)(1+n-1)/2 - (n-1) = (n-1)(n) - (n-1) = 
;;
;;n^2 - n - n + 1 = n^2 - 2n + 1 
;;
;;Penta:
;;
;;sum(5n-5) = 5*sum(1..n) - 5n = (5n^2 + 5n - 10n)/2
;;
;;1..n = n(1+n)/2 
;;
;;Totalt:
;;
;;(5n^2 + 5n - 10n - 2n^2 + 4n - 2)/ 2
;;
;;(3n^2 - n - 2) / 2