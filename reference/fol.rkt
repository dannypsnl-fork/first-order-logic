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

(define-pass subst : FOL (e subst-map) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(,v ,v* ...)
         (define (replace v)
           (if (assoc v subst-map)
               `(,(cdr (assoc v subst-map)) ,v)
               v))
         `(,(replace v) ,(map replace v*) ...)]))
(define-pass skolem : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(∃ (,v ...) ,e)
         (define vs (map (λ (v) (gensym 'Skolem)) v))
         (subst e (zip v vs))])
  (Expr e))

(define-pass remove-∀ : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(∀ (,v ...) ,e) e]))

(define-pass distribute-and : FOL (e) -> FOL ()
  (Expr : Expr (e) -> Expr ()
        [(or (and ,e0 ,e1) ,e2)
         `(and (or ,e0 ,e2)
               (or ,e1 ,e2))]))

(define-language CNF
  (extends FOL)
  (Expr (e)
        (- (∀ (v ...) e)
           (∃ (v ...) e)
           (->> e0 e1))))
(define-pass convert->CNF : FOL (e) -> CNF ()
  (Expr : Expr (e) -> Expr ()))

(define-parser parse-FOL FOL)
(define target '(∀ (x)
                   (->> (∀ (y) (->> (Animal y)
                                    (Loves x y)))
                        (∃ (y) (Loves y x)))))

(define (fol->cnf e)
  ((compose convert->CNF
            distribute-and
            remove-∀
            skolem
            move-in-not-2
            move-in-not-1
            move-in-not
            remove-implication
            parse-FOL)
   e))

(fol->cnf target)
