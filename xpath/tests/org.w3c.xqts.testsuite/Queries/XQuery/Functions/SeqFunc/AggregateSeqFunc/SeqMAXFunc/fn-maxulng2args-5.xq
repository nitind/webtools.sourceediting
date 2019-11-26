(:*******************************************************:)
(:Test: maxulng2args-5                                    :)
(:Written By: Carmelo Montanez                            :)
(:Date: Fri Dec 10 10:15:48 GMT-05:00 2004                :)
(:Purpose: Evaluates The "max" function                  :)
(: with the arguments set as follows:                    :)
(:$arg1 = xs:unsignedLong(lower bound)                   :)
(:$arg2 = xs:unsignedLong(upper bound)                   :)
(:*******************************************************:)

fn:max((xs:unsignedLong("0"),xs:unsignedLong("184467440737095516")))