(def modBase 1000000007)
(def right 0)
(def down  1)
(def taskRow 0)
(def taskCol 0)
(def cache (hash-map))

(defn maxTurns [row col]
	(-
		(* (- (min row col) 1) 2)
		(if (= row col) 1 0)
	)
)

(defn minTurns [dir row col]
	(if (or (= row 1) (= col 1))
		0
		1
	)
)

(defn cacheKey [turns dir row col]
	(let [dirs [row col]]
		[turns (nth dirs dir) (nth dirs (- 1 dir))])
)

(defn pathsToArrive [turns dir row col]
	(def cacheHit nil)
	;(println turns dir row col "...")
	(let [result (cond
			(or (< turns 0) (< row 1) (< col 1) (> row taskRow) (> col taskCol))
				(let [] 0)
			(= 0 turns)
				(if (and 	(>= (get [row col] (- 1 dir)) 1)
							(=  (get [row col] dir)		  1))
					1	0)
			(> turns (maxTurns row col))
				(let [] 0)
			(not (nil? (let [] (def cacheHit (get cache (cacheKey turns dir row col))) cacheHit)))
				cacheHit	
			(= right dir)
				(let [	paths [
							(pathsToArrive turns 		right row (- col 1) )
							(pathsToArrive (- turns 1) 	down  row (- col 1) )]
						sum (mod (reduce + paths) modBase)
					]
					;(println turns dir row col)
					(def cache (assoc cache (cacheKey turns dir row col) sum))
					sum
				)
			:else
				(let [	paths [
						(pathsToArrive turns 		down  	(- row 1) col)
						(pathsToArrive (- turns 1) 	right  	(- row 1) col)]
						sum (mod (reduce + paths) modBase)
					]
					;(println turns dir row col)
					(def cache (assoc cache (cacheKey turns dir row col) sum))
					sum
				)
		)]
		;(println turns dir row col result)
		result
	)
)

(defn pathCount [turns row col]
	(if (or (= row 1) (= col 1))
		(if (= turns 0) 1 0)
		(let []
			(def sum 0)
			(def sum (mod (+ sum (pathsToArrive (- turns 1) 	right (- row 1) 	col)) 			modBase)) ;(println sum)
			(def sum (mod (+ sum (pathsToArrive (- turns 1) 	down  row 			(- col 1))) 	modBase)) ;(println sum)
			(def sum (mod (+ sum (pathsToArrive turns 			right row 			(- col 1))) 	modBase)) ;(println sum)
			(def sum (mod (+ sum (pathsToArrive turns 			down  (- row 1) 	col)) 			modBase)) ;(println sum)
			sum
		)
	)
)

(defn solve [maxTurns row col]
	(def sum 0)
	(doseq [turns (range 0 (+ maxTurns 1))]
		(def sum (mod (+ sum (pathCount turns row col)) modBase))
	)
	sum
)

(read-line)
(def line (read-line))
(while (not (empty? line))
	(def task (map read-string (re-seq #"\d+" line)))
	(def taskRow (nth task 0))
	(def taskCol (nth task 1))
	(def taskTurns (nth task 2))
	(println (solve taskTurns taskRow taskCol))
	(def line (read-line))
)

;(println cache)

