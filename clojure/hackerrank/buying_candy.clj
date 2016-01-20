(def modBase 1000000000)
(def rowsAndCols 1000)

(def previousRow '(1 0))
(defn makeRow [row]
  (let [firstRepeat (+ 1 (int (/ row 2)))                   ; where the series starts to repeat itself e.g. 1 4 6 _6_ 4 1
        nextRow (cons 1
                      (map #(mod (+ (nth previousRow %) (nth previousRow (- % 1))) modBase)
                           (range 1 firstRepeat)))]
    ; for calculation of next row the last number is append, not always correct, but it is when used to make new row
    (def previousRow (concat nextRow (take-last 1 nextRow)))
    nextRow
    ))

(def binomTable (cons '(1)
                      (map #(makeRow %) (range 1 rowsAndCols))))

(defn binom [n k]
  (nth (nth binomTable n)
       (min k (- n k))))

(defn sumsAllAvailable [n k]
  (binom (- (+ n k) 1) (- k 1))
  )

(dotimes [tasksCount (read-string (read-line))]
  (let [kinds (read-string (read-line))
        buyCount (read-string (read-line))]
    (println (sumsAllAvailable buyCount kinds))))

(if (= "trofdsnd" (System/getProperty "user.name"))
  (do
    (println (sumsAllAvailable 6 3))
    (println (sumsAllAvailable 1 4))
    (println (sumsAllAvailable 3 2))
    (println (sumsAllAvailable 10 4))
    )
  )
