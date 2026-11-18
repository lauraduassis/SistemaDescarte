import JsBarcode from "jsbarcode";

export default function codigoBarras() {
    return {
        init() {
            const codigo = this.$el.dataset.codigo;
            if (!codigo) return;

            const ean13Valido = /^\d{13}$/.test(codigo)
                && [...codigo].reduce((soma, digito, indice) =>
                    soma + Number(digito) * (indice % 2 === 0 ? 1 : 3), 0) % 10 === 0;

            JsBarcode(this.$refs.barcode, codigo, {
                format: ean13Valido ? "EAN13" : "CODE128",
                displayValue: false,
                valid: valido => { this.$refs.barcode.hidden = !valido; }
            });
        }
    };
}
