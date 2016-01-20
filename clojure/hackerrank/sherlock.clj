(def modBase 1000000007)
(def rowsAndCols 100)

(def tempTrans (transient '[]))
(dotimes [n (+ rowsAndCols 1)]
  (conj! tempTrans (vec (repeat (+ rowsAndCols 1) 0))))

(def binomMatrix (to-array-2d (persistent! tempTrans)))

(dotimes [n rowsAndCols] (aset binomMatrix 0 n 1))
(dotimes [n rowsAndCols] (aset binomMatrix n n 1))          ; 1's occupy the diagonal
(doseq [row (range 1 rowsAndCols)]
  (doseq [col (range (+ row 1) rowsAndCols)]
    (aset binomMatrix row col
          (mod (+ (aget binomMatrix row (- col 1)) (aget binomMatrix (- row 1) (- col 1)))
               modBase))))

(defn binom [n k]
  (aget binomMatrix k n))

(defn sumsAllAvailable [s n]
  (if (> n 0) (binom (- (+ s n) 1) s)
              0))                                           ; 1 0; 0 1


(defn sumsNoZero [s n]
  (cond (>= s n) (sumsAllAvailable (- s n) n)
        :else 0)
  )

(defn sums [s k n]
  (reduce #(mod (+ %1 %2) modBase) 0 (map #(* (sumsNoZero s %) (binom n %)) (range 1 (+ k 1))))
  )

(defn sumsOneFixed [s k n]
  (reduce #(mod (+ %1 %2) modBase) 0
          (map #(* (sumsNoZero s %) (binom (- n 2) (- % 1))) (range 1 (+ k 1))))
  )

(defn sumsTwoFixed [s k n]
  (reduce #(mod (+ %1 %2) modBase) 0
          (map #(* (sumsNoZero s %) (binom (- n 2) (- % 2))) (range 2 (+ k 1))))
  )

(defn solveSNT [s n t]
  (let [kOneFixed (+ 1 (min (- n 2) (Math/floor (/ (- t 1) 2))))
        kTwoFixed (+ 2 (min (- n 2) (Math/floor (/ (- t 2) 2))))
        kNoEdges (min (- n 2) (Math/floor (/ t 2)))
        a (sumsOneFixed s kOneFixed n)
        b (sumsTwoFixed s kTwoFixed n)
        c (sums s kNoEdges (- n 2))]

    (cond (< n 2) n
          (= s 0) 1
          :else (mod (+ (* 2 (mod a modBase))
                        (if (> t 1) (mod b modBase) 0)
                        (mod c modBase))
                     modBase))
    ))

(defn solveRCT [taskRow taskCol taskTurns]
  (solveSNT (- (max taskRow taskCol) 1)
            (min taskRow taskCol)
            taskTurns)
  )


(defn readAndSolve []
  (read-line)
  (def line (read-line))
  (while (not (empty? line))
    (let [
          task (map read-string (re-seq #"\d+" line))
          taskRow (nth task 0)
          taskCol (nth task 1)
          taskTurns (nth task 2)]

      (println (solveRCT taskRow taskCol taskTurns))
      (def line (read-line))
      )
    )
  )

(readAndSolve)

(defn compareSlowAndFast []
  (dotimes [n 2]
    (def sum 0)
    (let [s (rand-int 50)
          n 5
          t (rand-int 50)
          ]
      (doseq [a (range 0 (+ s 1))]
        (doseq [b (range 0 (+ s 1))]
          (doseq [c (range 0 (+ s 1))]
            (doseq [d (range 0 (+ s 1))]
              (doseq [e (range 0 (+ s 1))]
                (if (and (= s (+ a b c d e))
                         (<= (reduce + 0 (map * (map #(if (> % 0) 1 0) (vector a b c d e))
                                              '(1 2 2 2 2 1))) t))
                  (let []
                    ;(println a b c d)
                    (def sum (+ sum 1))
                    )
                  )
                )
              )
            )
          )
        )
      (let [fastSum (solveSNT s n t)]
        (assert (= sum fastSum) [s n t sum fastSum]))
      )
    ))

;(compareSlowAndFast)
