(:*******************************************************:)
(: Test: K-DayTimeDurationDivide-10                      :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:36+02:00                       :)
(: Purpose: The division operator is not available between xs:integer and xs:dayTimeDuration. :)
(:*******************************************************:)
3 div xs:dayTimeDuration("P3D")