import JsBarcode from "jsbarcode";

export default function codigoBarras(codigo) {
    return {
        codigo,

        init() {
            if (!this.codigo) return;

            JsBarcode(this.$refs.barcode, this.codigo, {
                format: "EAN13",
                displayValue: false
            });
        }
    };
}

