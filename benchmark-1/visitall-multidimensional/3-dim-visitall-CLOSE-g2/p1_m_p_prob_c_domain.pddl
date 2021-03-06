(define (domain test)
(:predicates
	(at-robot-p3-p0-p0)
	(visited-p0-p1-p2)
	(visited-p6-p0-p0)
	(at-robot-p0-p0-p10)
	(at-robot-p2-p1-p0)
	(visited-p0-p1-p1)
	(visited-p0-p1-p0)
	(at-robot-p1-p0-p0)
	(at-robot-p0-p0-p7)
	(at-robot-p1-p0-p1)
	(at-robot-p0-p0-p6)
	(visited-p1-p0-p2)
	(visited-p1-p0-p1)
	(at-robot-p0-p0-p3)
	(at-robot-p10-p0-p0)
	(at-robot-p0-p0-p4)
	(visited-p1-p0-p0)
	(at-robot-p0-p1-p2)
	(visited-p10-p0-p0)
	(visited-p3-p0-p0)
	(at-robot-p0-p1-p0)
	(at-robot-p0-p1-p1)
	(visited-p2-p1-p0)
	(at-robot-p2-p0-p1)
	(visited-p0-p0-p10)
	(at-robot-p7-p0-p0)
	(visited-p0-p0-p1)
	(visited-p0-p0-p2)
	(visited-p0-p0-p3)
	(visited-p0-p0-p4)
	(at-robot-p2-p0-p0)
	(at-robot-p4-p0-p0)
	(visited-p0-p0-p0)
	(visited-p1-p1-p0)
	(at-robot-p1-p1-p0)
	(visited-p0-p0-p6)
	(visited-p0-p0-p7)
	(at-robot-p0-p0-p1)
	(at-robot-p6-p0-p0)
	(at-robot-p0-p0-p2)
	(at-robot-p0-p0-p0)
	(visited-p2-p0-p0)
	(at-robot-p1-p0-p2)
	(visited-p2-p0-p1)
	(visited-p4-p0-p0)
	(visited-p7-p0-p0)
)
(:action move-2-p0-p0-p0-p1
	:precondition (and (at-robot-p0-p0-p0)  )
	:effect (and (at-robot-p0-p0-p1) (visited-p0-p0-p1) (not (at-robot-p0-p0-p0))  )
)
(:action move-0-p0-p0-p1-p1
	:precondition (and (at-robot-p0-p0-p1)  )
	:effect (and (at-robot-p1-p0-p1) (visited-p1-p0-p1) (not (at-robot-p0-p0-p1))  )
)
(:action move-0-p2-p0-p0-p6
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (at-robot-p6-p0-p0) (visited-p6-p0-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-0-p2-p0-p0-p0
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (at-robot-p0-p0-p0) (visited-p0-p0-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-2-p0-p0-p2-p4
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (visited-p0-p0-p4) (at-robot-p0-p0-p4) (not (at-robot-p0-p0-p2))  )
)
(:action move-0-p0-p0-p0-p1
	:precondition (and (at-robot-p0-p0-p0)  )
	:effect (and (at-robot-p1-p0-p0) (visited-p1-p0-p0) (not (at-robot-p0-p0-p0))  )
)
(:action move-1-p2-p0-p0-p1
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (at-robot-p2-p1-p0) (visited-p2-p1-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-0-p1-p0-p0-p0
	:precondition (and (at-robot-p1-p0-p0)  )
	:effect (and (at-robot-p0-p0-p0) (visited-p0-p0-p0) (not (at-robot-p1-p0-p0))  )
)
(:action move-1-p0-p0-p2-p1
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (at-robot-p0-p1-p2) (visited-p0-p1-p2) (not (at-robot-p0-p0-p2))  )
)
(:action move-2-p0-p0-p2-p3
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (visited-p0-p0-p3) (at-robot-p0-p0-p3) (not (at-robot-p0-p0-p2))  )
)
(:action move-0-p2-p0-p0-p4
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (at-robot-p4-p0-p0) (visited-p4-p0-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-1-p1-p0-p0-p1
	:precondition (and (at-robot-p1-p0-p0)  )
	:effect (and (visited-p1-p1-p0) (at-robot-p1-p1-p0) (not (at-robot-p1-p0-p0))  )
)
(:action move-1-p0-p0-p1-p1
	:precondition (and (at-robot-p0-p0-p1)  )
	:effect (and (at-robot-p0-p1-p1) (visited-p0-p1-p1) (not (at-robot-p0-p0-p1))  )
)
(:action move-0-p2-p0-p0-p3
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (at-robot-p3-p0-p0) (visited-p3-p0-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-2-p0-p0-p2-p1
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (at-robot-p0-p0-p1) (visited-p0-p0-p1) (not (at-robot-p0-p0-p2))  )
)
(:action move-2-p2-p0-p0-p1
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (visited-p2-p0-p1) (at-robot-p2-p0-p1) (not (at-robot-p2-p0-p0))  )
)
(:action move-2-p0-p0-p2-p7
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (at-robot-p0-p0-p7) (visited-p0-p0-p7) (not (at-robot-p0-p0-p2))  )
)
(:action move-2-p0-p0-p1-p2
	:precondition (and (at-robot-p0-p0-p1)  )
	:effect (and (at-robot-p0-p0-p2) (visited-p0-p0-p2) (not (at-robot-p0-p0-p1))  )
)
(:action move-1-p0-p0-p0-p1
	:precondition (and (at-robot-p0-p0-p0)  )
	:effect (and (at-robot-p0-p1-p0) (visited-p0-p1-p0) (not (at-robot-p0-p0-p0))  )
)
(:action move-2-p0-p0-p2-p10
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (at-robot-p0-p0-p10) (visited-p0-p0-p10) (not (at-robot-p0-p0-p2))  )
)
(:action move-2-p0-p0-p2-p0
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (at-robot-p0-p0-p0) (visited-p0-p0-p0) (not (at-robot-p0-p0-p2))  )
)
(:action move-0-p2-p0-p0-p10
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (visited-p10-p0-p0) (at-robot-p10-p0-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-2-p0-p0-p2-p6
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (at-robot-p0-p0-p6) (visited-p0-p0-p6) (not (at-robot-p0-p0-p2))  )
)
(:action move-2-p1-p0-p0-p1
	:precondition (and (at-robot-p1-p0-p0)  )
	:effect (and (at-robot-p1-p0-p1) (visited-p1-p0-p1) (not (at-robot-p1-p0-p0))  )
)
(:action move-0-p1-p0-p0-p2
	:precondition (and (at-robot-p1-p0-p0)  )
	:effect (and (visited-p2-p0-p0) (at-robot-p2-p0-p0) (not (at-robot-p1-p0-p0))  )
)
(:action move-0-p2-p0-p0-p7
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (visited-p7-p0-p0) (at-robot-p7-p0-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-0-p0-p0-p2-p1
	:precondition (and (at-robot-p0-p0-p2)  )
	:effect (and (at-robot-p1-p0-p2) (visited-p1-p0-p2) (not (at-robot-p0-p0-p2))  )
)
(:action move-0-p2-p0-p0-p1
	:precondition (and (at-robot-p2-p0-p0)  )
	:effect (and (at-robot-p1-p0-p0) (visited-p1-p0-p0) (not (at-robot-p2-p0-p0))  )
)
(:action move-2-p0-p0-p1-p0
	:precondition (and (at-robot-p0-p0-p1)  )
	:effect (and (at-robot-p0-p0-p0) (visited-p0-p0-p0) (not (at-robot-p0-p0-p1))  )
)

)