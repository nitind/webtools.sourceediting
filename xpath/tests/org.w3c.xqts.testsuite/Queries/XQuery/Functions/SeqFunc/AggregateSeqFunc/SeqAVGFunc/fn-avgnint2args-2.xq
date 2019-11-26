(:*******************************************************:)
(:Test: avgnint2args-2                                    :)
(:Written By: Carmelo Montanez                            :)
(:Date: Fri Dec 10 10:15:47 GMT-05:00 2004                :)
(:Purpose: Evaluates The "avg" function                  :)
(: with the arguments set as follows:                    :)
(:$arg1 = xs:negativeInteger(mid range)                  :)
(:$arg2 = xs:negativeInteger(lower bound)                :)
(:*******************************************************:)

fn:avg((xs:negativeInteger("-297014075999096793"),xs:negativeInteger("-999999999999999999")))