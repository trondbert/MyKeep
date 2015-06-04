(def modBase 100000007)

(def fibMap {0 0, 1 1})
(declare fib)

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

(defn fibFast [t n] ; t is the target, n is temporary
    (if (< t 3) (fib t)
        (if (< (- t n) 20) ; not worth doing smart stuff 
            (fib t)
            (let [fn            (fib n)
                  fm            (get fibMap (- n 1))
                  d             (largestStoredFib (- t n))
                  fd            (get fibMap    d)
                  fd_add1       (get fibMap (+ d 1))
                  fd_sub1       (get fibMap (- d 1))
                  fn_add_d      (mod (+ (* fn fd_add1) (* fm fd)) modBase)
                  fn_add_d_sub1 (mod (+ (* fn fd) (* fm fd_sub1)) modBase)
                  fn_add_d_add1 (mod (+ fn_add_d fn_add_d_sub1) modBase)
                 ]
                 (def fibMap (assoc fibMap  (+ n d)     fn_add_d 
                                            (+ n d -1)  fn_add_d_sub1
                                            (+ n d 1)   fn_add_d_add1))
                 ;fib (n + d) = fn*fib (d + 1) + fm * fib d
                 ;fib (n + d-1) = fn*fib (d) + fm * fib (d-1)
                 
                 (fib t (+ n d))
            )
        )
    )
)

(defn fib [n]
    (if (> n 20) 
        (fibFast n (largestStoredFib n))
        (if (contains? fibMap n)
            (get fibMap n)
            (let [result (mod (+ (fib (- n 1)) (fib (- n 2))) modBase)]
                (def fibMap (assoc fibMap n result))
                result
            )
        )
    )
)

;(doseq [line (rest (line-seq (java.io.BufferedReader. *in*)))]
;    (println (fib (read-string line))))

(println (fib 10 0))
