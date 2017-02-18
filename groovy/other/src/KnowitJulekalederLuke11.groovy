import java.time.LocalDateTime
import java.time.ZoneOffset

import static java.time.temporal.ChronoUnit.SECONDS

long intMax = Integer.MAX_VALUE
println intMax

int days = (intMax / (60*60*25)) as int
def dayOfOverflow = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).plusDays(days)
println (dayOfOverflow)

int restSeconds = intMax - (days * (60*60*25))
println ((dayOfOverflow.plus(restSeconds, SECONDS)).toString().replaceAll(":", "-") + "Z")

