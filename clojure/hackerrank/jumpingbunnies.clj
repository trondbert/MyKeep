
(defn divideIfDivisible [number divisor divisible]
    (if divisible (/ number divisor) number)
)

(defn gcd [numA, numB]
    (def a numA) (def b numB)
    (while (> b 0)
        (def t b)
        (def b (mod a t))
        (def a t))
    a
)

(defn solve [jumps acc]
    ;(println "solve" jumps acc)
    (def continue true)
    (def indexA 0) (def indexB 0) (def jumpsCount (count jumps))
    (def result 0)
    (while continue (let [jumpA (nth jumps indexA)]
        (def indexB (+ indexA 1)) (def continue (< indexB jumpsCount))

        (while continue (let [jumpB (nth jumps indexB) gcdJumps (gcd jumpA jumpB)]
            ;(println jumpA jumpB gcdJumps)
            (if (> gcdJumps 1)
                (let    [divisbleList   (map #(= 0 (mod % gcdJumps)) jumps) 
                         divided        (map divideIfDivisible jumps (repeat gcdJumps) divisbleList)]
                    (def continue false)
                    (def result (solve (distinct divided) (* acc gcdJumps)) ))
            )
            (def indexB (+ indexB 1)) (def continue (< indexB jumpsCount))
        ))
        (def indexA (+ indexA 1)) (def continue (< indexA jumpsCount))
    ))
    (if (> result 0) result (* acc (reduce * jumps)))
)

(read-line)
(let [jumps (sort (map read-string (re-seq #"\d+" (read-line))))]
    (println (solve jumps 1))
)
