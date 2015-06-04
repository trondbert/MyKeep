;fibonacci

(def modBase 100000007)

(def fibArr [0 1])

(defn fib [n]
    (if (< n 2)
        n
        (mod (+ (fib (- n 1)) (fib (- n 2))) modBase)
    )
)

(def foo (vector 1))

(assoc foo 4 "fds")

(doseq [line (rest (line-seq (java.io.BufferedReader. *in*)))]
    (println (fib (read-string line)))
)
