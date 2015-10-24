
(defn commonCharOrNil [a b]
	(if (= a b) a nil)
)

(defn commonPrefix [a b]
	(let [firstMismatch (.indexOf (map commonCharOrNil a b) nil)]
		(if (>= firstMismatch 0)
			(take firstMismatch a)
			(take (min (count a) (count b)) a)
		)
	)
)

(defn solve []
	(let [a (re-seq #"." (read-line))
		  b (re-seq #"." (read-line))
		  p (commonPrefix a b)
		  tailA (take-last (- (count a) (count p)) a)
		  tailB (take-last (- (count b) (count p)) b)]
		(print (count p) "")
		(doseq [charP p] (print charP))
		(println)
		(print (count tailA) "")
		(doseq [charA tailA] (print charA))
		(println)  
		(print (count tailB) "")
		(doseq [charB tailB] (print charB))
		(println)
	)
)

(solve)

(assert (= '("a") 		(commonPrefix '("a" "b") '("a"))))
(assert (= '("a" "b") 	(commonPrefix '("a" "b") '("a" "b" "c"))))
(assert (= '() 			(commonPrefix '("a" "b") '("d"))))
(assert (= '("a") 		(commonPrefix '("a" "b") '("a" "q"))))
