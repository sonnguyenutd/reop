
(define (problem visitall-3-dim-p-4)
(:domain visitall-3-dim)
(:objects 
	p0 p1 p2 p3 p4 p5 p6 p7 p8 p9 p10 p11 p12 p13 p14 p15 p16 p17 p18 p19 p20 p21 p22 p23 p24 p25 p26 p27 p28 p29 - pos
)
(:init
(neighbor p5 p17)
(neighbor p13 p24)
(neighbor p4 p20)
(neighbor p10 p18)
(neighbor p21 p0)
(neighbor p5 p21)
(neighbor p13 p0)
(neighbor p24 p10)
(neighbor p16 p26)
(neighbor p18 p7)
(neighbor p13 p8)
(neighbor p12 p19)
(neighbor p16 p6)
(neighbor p3 p27)
(neighbor p4 p6)
(neighbor p12 p4)
(neighbor p28 p24)
(neighbor p17 p3)
(neighbor p1 p13)
(neighbor p2 p14)
(neighbor p20 p3)
(neighbor p7 p2)
(neighbor p3 p20)
(neighbor p0 p27)
(neighbor p18 p28)
(neighbor p18 p16)
(neighbor p11 p1)
(neighbor p14 p11)
(neighbor p9 p24)
(neighbor p25 p11)
(neighbor p4 p7)
(neighbor p17 p27)
(neighbor p1 p25)
(neighbor p25 p1)
(neighbor p15 p12)
(neighbor p23 p3)
(neighbor p10 p12)
(neighbor p24 p16)
(neighbor p25 p12)
(neighbor p13 p6)
(neighbor p14 p10)
(neighbor p26 p8)
(neighbor p18 p13)
(neighbor p7 p12)
(neighbor p2 p21)
(neighbor p28 p14)
(neighbor p19 p11)
(neighbor p8 p16)
(neighbor p1 p6)
(neighbor p17 p5)
(neighbor p27 p15)
(neighbor p26 p22)
(neighbor p21 p15)
(neighbor p26 p18)
(neighbor p8 p20)
(neighbor p26 p7)
(neighbor p24 p27)
(neighbor p11 p7)
(neighbor p21 p27)
(neighbor p7 p19)
(neighbor p21 p5)
(neighbor p18 p0)
(neighbor p7 p23)
(neighbor p2 p20)
(neighbor p4 p11)
(neighbor p4 p23)
(neighbor p28 p25)
(neighbor p20 p11)
(neighbor p13 p20)
(neighbor p10 p14)
(neighbor p14 p9)
(neighbor p1 p10)
(neighbor p16 p18)
(neighbor p0 p10)
(neighbor p18 p23)
(neighbor p6 p13)
(neighbor p12 p0)
(neighbor p27 p17)
(neighbor p20 p12)
(neighbor p26 p20)
(neighbor p16 p21)
(neighbor p19 p25)
(neighbor p15 p4)
(neighbor p0 p19)
(neighbor p2 p8)
(neighbor p6 p12)
(neighbor p15 p28)
(neighbor p22 p11)
(neighbor p0 p9)
(neighbor p17 p19)
(neighbor p8 p5)
(neighbor p25 p8)
(neighbor p1 p20)
(neighbor p5 p9)
(neighbor p7 p1)
(neighbor p16 p28)
(neighbor p11 p25)
(neighbor p0 p16)
(neighbor p8 p25)
(neighbor p6 p11)
(neighbor p11 p2)
(neighbor p16 p4)
(neighbor p25 p20)
(neighbor p3 p0)
(neighbor p0 p20)
(neighbor p25 p28)
(neighbor p23 p7)
(neighbor p6 p4)
(neighbor p2 p24)
(neighbor p10 p15)
(neighbor p26 p3)
(neighbor p8 p12)
(neighbor p15 p18)
(neighbor p23 p26)
(neighbor p18 p10)
(neighbor p14 p6)

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
(neighbor p5 p6)
(neighbor p6 p5)
(neighbor p6 p7)
(neighbor p7 p6)
(neighbor p7 p8)
(neighbor p8 p7)
(neighbor p8 p9)
(neighbor p9 p8)
(neighbor p9 p10)
(neighbor p10 p9)
(neighbor p10 p11)
(neighbor p11 p10)
(neighbor p11 p12)
(neighbor p12 p11)
(neighbor p12 p13)
(neighbor p13 p12)
(neighbor p13 p14)
(neighbor p14 p13)
(neighbor p14 p15)
(neighbor p15 p14)
(neighbor p15 p16)
(neighbor p16 p15)
(neighbor p16 p17)
(neighbor p17 p16)
(neighbor p17 p18)
(neighbor p18 p17)
(neighbor p18 p19)
(neighbor p19 p18)
(neighbor p19 p20)
(neighbor p20 p19)
(neighbor p20 p21)
(neighbor p21 p20)
(neighbor p21 p22)
(neighbor p22 p21)
(neighbor p22 p23)
(neighbor p23 p22)
(neighbor p23 p24)
(neighbor p24 p23)
(neighbor p24 p25)
(neighbor p25 p24)
(neighbor p25 p26)
(neighbor p26 p25)
(neighbor p26 p27)
(neighbor p27 p26)
(neighbor p27 p28)
(neighbor p28 p27)
(neighbor p28 p29)
(neighbor p29 p28)
)
(:goal
(and 
	(visited p0 p6 p5)
(visited p0 p2 p9)
)
)
)
