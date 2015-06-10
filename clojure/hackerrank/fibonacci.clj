(def modBase 100000007)

(def fibMap     (vector 1 1 3 21))
(def prevFibMap (vector 0 1 2 13))
(def fibArr     (vector 0 1 1  2))
(declare fib)

(defn log2 [n]
  (int (/ (Math/log n) (Math/log 2)))
)

(def not-empty? (complement empty?))

; Largest stored fibonacci key x. Makes sure that values for x-1 and x+1 are also calculated
(defn largestStoredFib [highLimit]
    (let [fibKeys (keys fibMap)
          largest (apply max (filter #(< % highLimit) fibKeys))]
        (if (not-empty? (filter #(= % (- largest 1)) fibKeys))
            (let [fx_add1 (mod (+ (get fibMap largest) (get fibMap (- largest 1))) modBase)] 
                (def fibMap (assoc fibMap (+ largest 1) fx_add1))
                largest
            )
            (if (not-empty? (filter #(= % (+ largest 1)) fibKeys))
                (let [fx_sub1 (mod (- (get fibMap (+ largest 1)) (get fibMap largest)) modBase)] 
                    (def fibMap (assoc fibMap (- largest 1) fx_sub1))
                    largest
                )
                (largestStoredFib (- highLimit 1))
            )
        )
    )
)

(defn bestDiffLog [distance]
    (min (int (log2 distance)) (- (count fibMap) 1))
)

(defn fibFast [n t ft fs] ; n is the final target, t is temporary
    (if (= n t)
        ft
        (let [d             (int (Math/pow 2 (bestDiffLog (- n t))))
              tLog (log2 t)
              dLog (log2 d)
              fd            (fibMap dLog)
              fd_sub1       (prevFibMap dLog)
              ft_add_d      (mod (+ (* (+ ft fs) fd) (* ft fd_sub1)) modBase)
              ft_add_d_sub1 (mod (+ (* ft fd) (* fs fd_sub1)) modBase)
             ]
            (println [n t ft fs])            (println ["d: " d (- n t)])
            (if (= d t)
                (let [] (def fibMap (conj fibMap ft_add_d))
                        (def prevFibMap (conj prevFibMap ft_add_d_sub1)))
            )
            (println ["Map " fibMap])
            (println ["Map " prevFibMap])
            (println ["kaller " n (+ t d) ft_add_d ft_add_d_sub1])
            (fibFast n (+ t d) ft_add_d ft_add_d_sub1)
        )
    )
)

(defn fib [n]
    (if (< n 2)
        (fibArr n)
        (let [tLog (bestDiffLog n) 
              t  (int (Math/pow 2 (bestDiffLog n)))
              ft (fibMap tLog)
              fs (prevFibMap tLog)
             ]
            (fibFast n t ft fs)
        )
    )
)

(doseq [line (rest (line-seq (java.io.BufferedReader. *in*)))]
   (println (fib (read-string line))))

;(println (bestDiff 10))
;(println (fib 7))
;(println fibMap)
;(println (int (Math/pow 2 (bestDiffLog 3))))