(:*******************************************************:)
(: Test: K-SeqExprCastable-17                            :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:39+02:00                       :)
(: Purpose: A test whose essence is: `not(QName("", "lname") castable as xs:integer)`. :)
(:*******************************************************:)
not(QName("", "lname") castable as xs:integer)