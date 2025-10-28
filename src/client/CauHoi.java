    package client;

    class CauHoi {
        private int maCH;
        private String noiDung;
        private String dapan_a;
        private String dapan_b;
        private String dapan_c;
        private String dapan_d;
        private String dapandung;

        public CauHoi(int maCH, String noiDung, String a, String b, String c, String d, String dapAnDung) {
            this.maCH = maCH;
            this.noiDung = noiDung;
            this.dapan_a = a;
            this.dapan_b = b;
            this.dapan_c = c;
            this.dapan_d = d;
            this.dapandung = dapAnDung;
        }

        public int getMaCH() {
            return maCH;
        }

        public String getNoiDung() {
            return noiDung;
        }

        public String getA() {
            return dapan_a;
        }

        public String getB() {
            return dapan_b;
        }

        public String getC() {
            return dapan_c;
        }

        public String getD() {
            return dapan_d;
        }

        public String getDapAnDung() {
            return dapandung;
        }

        @Override
        public String toString() {
            return "CauHoi{" +
                    "maCH=" + maCH +
                    ", noiDung='" + noiDung + '\'' +
                    ", A='" + dapan_a + '\'' +
                    ", B='" + dapan_b + '\'' +
                    ", C='" + dapan_c + '\'' +
                    ", D='" + dapan_d + '\'' +
                    ", dapAnDung='" + dapandung + '\'' +
                    '}';
        }
    }
