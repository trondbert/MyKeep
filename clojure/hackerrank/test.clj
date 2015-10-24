(apply min-key #(= true %) '(false true false true))

(defn commonCharOrBlank [a b]
	(if (= a b) a "")
)

(let [a (re-seq #"." (read-line))
	  b (re-seq #"." (read-line))
	  firstDiff (.indexOf (map commonCharOrBlank a b) "")]
	(println firstDiff)  
	(for [it (take firstDiff a)] (print it))     
)
