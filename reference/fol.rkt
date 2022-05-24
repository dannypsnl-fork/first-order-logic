#lang racket
(require nanopass
         (only-in list-util zip))

(define-language FOL
  (terminals
   (symbol (v)))
  (Expr (e)
        (∀ (v ...) e)
        (∃ (v ...) e)
        (or e0 e1)
        (and e0 e1)
        (not e)
        (v e ...)
        v
        (->> e0 e1)))

(define-pass subst : FOL (e subst-map) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(,v ,v* ...)
         (define (replace v)
           (if (assoc v subst-map)
               (cdr (assoc v subst-map))
               v))
         `(,(replace v) ,(map replace v*) ...)]))
(define-pass uniquify : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(∃ (,v ...) ,[e])
         (define new-vs (map gensym v))
         `(∃ (,new-vs ...) ,(subst e (zip v new-vs)))]
        [(∀ (,v ...) ,[e])
         (define new-vs (map gensym v))
         `(∀ (,new-vs ...) ,(subst e (zip v new-vs)))]))

(define-pass remove-implication : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(->> ,[e0] ,[e1])
         `(or (not ,e0) ,e1)]))

(define-pass move-in-not : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(not (∀ (,v ...) ,[e]))
         `(∃ (,v ...) (not ,e))]
        [(not (∃ (,v ...) ,[e]))
         `(∀ (,v ...) (not ,e))]))
(define-pass move-in-not-1 : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(not (or ,[e0] ,[e1]))
         `(and (not ,e0) (not ,e1))]
        [(not (and ,[e0] ,[e1]))
         `(or (not ,e0) (not ,e1))]))
(define-pass move-in-not-2 : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(not (not ,[e])) e]))

(define-pass subst-skolem : FOL (e subst-map) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(,v ,v* ...)
         (define (replace v)
           (if (assoc v subst-map)
               `(,(cdr (assoc v subst-map)) ,v)
               v))
         `(,(replace v) ,(map replace v*) ...)]))
(define-pass skolem : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(∃ (,v ...) ,[e])
         (define vs (map (λ (v) (gensym 'Skolem)) v))
         (subst-skolem e (zip v vs))])
  (Expr e))

(define-pass remove-∀ : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(∀ (,v ...) ,[e]) e]))

(define-pass distribute-and : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(or (and ,[e0] ,[e1]) ,[e2])
         `(and (or ,e0 ,e2)
               (or ,e1 ,e2))]
        [(or ,[e2] (and ,[e0] ,[e1]))
         `(and (or ,e0 ,e2)
               (or ,e1 ,e2))]))

(define-language CNF
  (extends FOL)
  (Expr (e)
        (- (∀ (v ...) e)
           (∃ (v ...) e)
           (->> e0 e1)
           (or e0 e1)
           (and e0 e1))
        (+ (or e ...)
           (and e ...))))
(define-pass convert->CNF : FOL (e) -> CNF ()
  (Expr : Expr (e) -> Expr ()
        [(or ,[e0] ,[e1]) `(or ,e0 ,e1)]
        [(and ,[e0] ,[e1]) `(and ,e0 ,e1)]))

(define-pass compact-and/or : CNF (e) -> CNF ()
  (Expr : Expr (e) -> Expr ()
        [(or (or ,[e0] ,[e1]) ,[e2]) `(or ,e0 ,e1 ,e2)]
        [(and (and ,[e0] ,[e1]) ,[e2]) `(and ,e0 ,e1 ,e2)]))

(define-parser parse-CNF CNF)
(define-pass CNF->clauses : CNF (e) -> * ()
  (Expr : Expr (e) -> * ()
        [(or ,e* ...)
         (apply set-union (map CNF->clauses e*))]
        [(and ,e* ...)
         (map CNF->clauses e*)]
        [(not ,e0) (set (unparse-CNF e))]
        [(,v ,e* ...) (set (unparse-CNF e))]
        [,v (set)]))

(define (fol->cnf e)
  (define-parser parse-FOL FOL)

  ; optional pass, insert it to any FOL pass can check the convertion result
  (define (print-FOL e)
    (println (unparse-FOL e))
    e)

  ((compose CNF->clauses
            ; CNF passes
            compact-and/or
            convert->CNF
            ; FOL passes
            distribute-and
            remove-∀
            skolem
            move-in-not-2
            move-in-not-1
            move-in-not
            remove-implication
            uniquify
            parse-FOL)
   e))

(define target '(∀ (x)
                   (->> (∀ (y) (->> (Animal y)
                                    (Loves x y)))
                        (∃ (y) (Loves y x)))))
; (fol->cnf target)

(define (make-KB rules)
  (list->set (map fol->cnf rules)))
(define (resolve r1 r2)
  ; TODO: unification on sub rules in r1, r2
  (println r1)
  (println r2)
  (set))
(define (resolution kb-rules query)
  (define new '())
  (let/ec return
    (let loop ([kb (make-KB kb-rules)])
      (for ([c (in-combinations (set->list kb) 2)])
        (define resolvents (resolve (first c) (second c)))
        (if (set-empty? resolvents)
            (return #t)
            (set! new (set-union new resolvents))))
      (if (subset? new kb)
          (return #f)
          (loop (set-union kb new))))))
(resolution '((R Apple)
              (->> (R x) (S x)))
            '(S apple))
