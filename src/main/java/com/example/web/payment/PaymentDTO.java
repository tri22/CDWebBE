package com.example.web.payment;

public abstract class PaymentDTO {
    public static class VNPayResponse {
        private String code;
        private String message;
        private String paymentUrl;

        public VNPayResponse() {
        }

        public VNPayResponse(String code, String message, String paymentUrl) {
            this.code = code;
            this.message = message;
            this.paymentUrl = paymentUrl;
        }

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPaymentUrl() {
            return paymentUrl;
        }

        public void setPaymentUrl(String paymentUrl) {
            this.paymentUrl = paymentUrl;
        }

        // Builder
        public static VNPayResponseBuilder builder() {
            return new VNPayResponseBuilder();
        }

        public static class VNPayResponseBuilder {
            private String code;
            private String message;
            private String paymentUrl;

            public VNPayResponseBuilder code(String code) {
                this.code = code;
                return this;
            }

            public VNPayResponseBuilder message(String message) {
                this.message = message;
                return this;
            }

            public VNPayResponseBuilder paymentUrl(String paymentUrl) {
                this.paymentUrl = paymentUrl;
                return this;
            }

            public VNPayResponse build() {
                return new VNPayResponse(code, message, paymentUrl);
            }
        }
    }
}