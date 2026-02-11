package io.github.meowpowpng.chargeflowev.billing.api;

import io.github.meowpowpng.chargeflowev.billing.domain.BillingResult;
import io.github.meowpowpng.chargeflowev.session.api.FinalizedSession;

public interface BillingQuery {

    BillingResult calculateForSession(FinalizedSession session);
}
