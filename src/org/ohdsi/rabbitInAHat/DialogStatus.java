package org.ohdsi.rabbitInAHat;

/**
 * Created by q798470 on 7/7/2017.
 */
public class DialogStatus {
    boolean generateETLDocument;
    boolean generatePseudocodeSql;
    boolean sourceFillRates;
    boolean targetFillRates;
    boolean ok;

    public boolean isGenerateETLDocument() {
        return generateETLDocument;
    }

    public void setGenerateETLDocument(boolean generateETLDocument) {
        this.generateETLDocument = generateETLDocument;
    }

    public boolean isGeneratePseudocodeSql() {
        return generatePseudocodeSql;
    }

    public void setGeneratePseudocodeSql(boolean generatePseudocodeSql) {
        this.generatePseudocodeSql = generatePseudocodeSql;
    }

    public boolean isSourceFillRates() {
        return sourceFillRates;
    }

    public void setSourceFillRates(boolean sourceFillRates) {
        this.sourceFillRates = sourceFillRates;
    }

    public boolean isTargetFillRates() {
        return targetFillRates;
    }

    public void setTargetFillRates(boolean targetFillRates) {
        this.targetFillRates = targetFillRates;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    boolean cancel;
}
