(def foo (hash-map))
(def foo (assoc foo '(3 4 4) 4))
(println (get foo '(3 4 4)))
(println (def foo (seq [1 2 3])))

(def modBase 100000)
(def rowsAndCols 100)

(def tempTrans (transient '[]))
(dotimes [n rowsAndCols] (conj! tempTrans (vec (repeat rowsAndCols nil))))

; rad (1. index): hvilket tall vi vil ha, kolonne (2. index): hvor mange tall vi kan bruke i summen
(def combMatrix (to-array-2d (persistent! tempTrans)))

(dotimes [n rowsAndCols] (aset combMatrix 1 n 1))
(dotimes [n rowsAndCols] (aset combMatrix n 1 1))

(doseq [row (range 2 rowsAndCols)]
    (doseq [col (range 2 rowsAndCols)]
        (aset combMatrix row col
            (mod
                (+ (aget combMatrix row (- col 1)) (aget combMatrix (- row 1) col))
                modBase
            ))
    )
)

(doseq [row combMatrix]
    (let []
        (doseq [x row] (print x "\t"))
        (println)
    )
)
