
(defn debugln [& objects] 
nil
;(debugln objects)
)

(defn squared [num] (* num num))

(defn xPos [point]
    (nth point 0)
)
(defn yPos [point]
    (nth point 1)
)
(defn distance [p1, p2]
	(Math/sqrt (reduce + (map squared (map - p2 p1))))
)
(defn perimeter [points]
	(reduce +
		(map distance points 
					  (concat (rest points) (take 1 points)) ; each point rolled to the right
		))
)
(defn cosineWithPointAndXAxis [basePoint point]
	(if (= point basePoint)
		0
		( / (- (xPos point) (xPos basePoint))
			(distance basePoint point)
		)
	)
)
(defn ccwRotation [p1, p2, p3]
	(let [rotation 
		(- 	(* (- (xPos p2) (xPos p1)) (- (yPos p3) (yPos p1)))
	   		(* (- (yPos p2) (yPos p1)) (- (xPos p3) (xPos p1))))]
		rotation
	)
)
(defn lowerLeft [points]
	(let [minYPoint (apply min-key yPos points)]
		(apply min-key xPos (filter #(= (yPos %) (yPos minYPoint)) points))
	)
)

(defn sortByRotationThenDistance [points getCosine getDistance] 
	(sort 
		(fn [pHead pNext] (let [cosineDiff (- (getCosine pHead) (getCosine pNext))]
			(cond 	(< (Math/abs cosineDiff) 0.0000001)
						(> (getDistance pHead) (getDistance pNext))
					:else
						(> cosineDiff 0.0))
		))
		points
	)
)
(defn findHull [pointsPstnt, pStart]
	(def points (transient pointsPstnt))
	(def edge 1)
	(def pNext 2)
	(def start false)
	(while (not start)
		(let [a (nth points (- edge 1)) b (nth points edge) c (nth points pNext) rotation (ccwRotation a b c)]
			(if (< (Math/abs rotation) 0.0000001)
				(def pNext (+ pNext 1))
				(def start true)
			)
		)
	)
	(while (< pNext (count points))
		(let [a (nth points (- edge 1)) b (nth points edge) c (nth points pNext) rotation (ccwRotation a b c)]
			(cond 	(= rotation 0)
						(let [distances (map (partial distance pStart) [a b c])]
							()

							(assoc! points edge c) (def pNext (+ pNext 1)))
					(> rotation 0)
						(let [] (def edge (+ edge 1)) (assoc! points edge c) (def pNext (+ pNext 1)))
					:else
						(let [] (def edge (- edge 1)))
			)
		)
	)
	(def points (persistent! points))
	(def hull (subvec points 0 (+ edge 1)))
	hull
)
(defn solve [pointsRead]
	(let [pStart (lowerLeft pointsRead) getCosine (partial cosineWithPointAndXAxis pStart)
										getDistance (partial distance pStart)]
		(def points
			(concat [pStart] (remove
				#(= pStart %) 
				(sortByRotationThenDistance points getCosine getDistance))
		))
		
		(def hull (findHull (apply vector points) pStart))
		(perimeter hull)
	)
)
(defn readPoints []
	(def points [])
	(dotimes [n (read-string (read-line))]
	    (let [pointStr (re-seq #"\d+" (read-line)) point (apply vector (map read-string pointStr))]
	        (def points (conj points point))
	    )
	)
	points
)

(println (solve (readPoints)))
;(println (lowerLeft (readPoints)))
