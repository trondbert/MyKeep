(println
  (reduce #(concat %1 %2) '() '((1 2) (2 9))))

(defn c [a op b]
  (op a b))

(println (c 4 + 3))

; 0 1 2 3 4 5
; 5 0 4 1 3 2
; 1 3 5 4 2 0

; 0 1 2 3 4
; 4 0 3 1 2
; 1 3 4 2 0

; <= floor n/2
; 1 + n*2
; (N-1 - n) * 2

(println (cons 0 '(3 4 5)))

(println (cons 1
               (map #(* 3 %) (range 1 4))))

(def mylist '(3 5 2))
(println (concat mylist (take-last 1 mylist)))
