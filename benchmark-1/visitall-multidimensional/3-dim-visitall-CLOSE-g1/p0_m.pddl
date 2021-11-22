
(define (problem visitall-3-dim-p-0)
(:domain visitall-3-dim)
(:objects 
	p0 p1 p2 p3 p4 p5 - pos
)
(:init
(neighbor p4 p2)
(neighbor p4 p0)
(neighbor p4 p1)
(neighbor p1 p3)
(neighbor p2 p0)
(neighbor p0 p2)
(neighbor p2 p4)
(neighbor p3 p0)
(neighbor p3 p1)
(neighbor p0 p4)

;;;;
	(at-robot p0 p0 p0)
	(visited p0 p0 p0)
	(neighbor p0 p1)
(neighbor p1 p0)
(neighbor p1 p2)
(neighbor p2 p1)
(neighbor p2 p3)
(neighbor p3 p2)
(neighbor p3 p4)
(neighbor p4 p3)
(neighbor p4 p5)
(neighbor p5 p4)
)
(:goal
(and 
	(visited p1 p2 p0)
)
)
)
