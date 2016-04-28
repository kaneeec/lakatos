package cz.pikadorama.lakatos.app;

import java.util.ArrayList;
import java.util.List;

import cz.pikadorama.lakatos.R;

public enum Sound {
        ABYCH(R.raw.abych_mohl_toto, "Abys mohl toto!"),
        ANIOCKO(R.raw.ani_ocko_nenasadis, "Ani očko nenasadíš!"),
        ANIZAKOKOT(R.raw.ani_za_kokot_vole, "Ani za kokot vole!"),
        BANALNI(R.raw.banalni_vec, "Banální věc!"),
        DOPICE(R.raw.do_pice, "Do piče!"),
        HAZLI(R.raw.hajzli_jedni, "Hajzli jedni!"),
        HOSI(R.raw.hosi_to_je_neuveritelne, "Hoši, toto je neuvěřitelné!"),
        JASEPOJEBAT(R.raw.ja_se_z_toho_musim_pojebat, "Já se z toho musim pojebat!"),
        JATOMRDAM(R.raw.ja_to_mrdam, "Já to mrdám!"),
        JATONEJDUDELAT(R.raw.ja_to_nejdu_delat, "Já to nejdu dělat!"),
        JEDINOUPICOVINKU(R.raw.jedinou_picovinku, "Jedinou pičovinku!"),
        JEDUDOPICI(R.raw.jedu_do_pici_stadyma, "Jedu do piči!"),
        KURVA(R.raw.kurva, "KURVA!"),
        KURVADOPICE(R.raw.kurva_do_pice_to_neni_mozne, "To není možné!"),
        KURVAUZ(R.raw.kurva_uz, "Kurva už!"),
        NENENASADIS(R.raw.ne_nenasadis_ho, "Ne, nenasadíš ho!"),
        NEBUDU(R.raw.nebudu_to_delat, "Nebudu to dělat!"),
        NEJVETSI(R.raw.nejvetsi_blbec_na_zemekouli, "Největší blbec na zeměkouli!"),
        NENASADIM(R.raw.nenasadim, "Nenasadím!"),
        NERESITELNY(R.raw.neresitelny_problem_hosi, "Neřešitelný problém hoši!"),
        NEVIMJAK(R.raw.nevim_jak, "Nevim jak!"),
        OKAMZITE(R.raw.okamzite_zabit_ty_kurvy, "Okamžitě zabít ty kurvy!"),
        PASTVEDLE(R.raw.past_vedle_pasti_pico, "Past vedle pasti!"),
        POCKEJKAMO(R.raw.pockej_kamo, "Počkej kámo!"),
        TADYMUSIS(R.raw.tady_musis_vsechno_rozdelat, "Tady musíš všechno rozdělat!"),
        TOJEPICO(R.raw.to_je_pico_nemozne, "To je nemožné!"),
        TONENI(R.raw.to_neni_normalni_kurva, "To neni normální!"),
        TOSOU(R.raw.to_sou_nervy_ty_pico, "To sou nervy ty pičo!"),
        TUTOPICU(R.raw.tuto_picu_potrebuju_utahnout, "Tuto piču potřebuju utáhnout!"),
        ZASRANE(R.raw.zasrane_zamrdane, "Zasrané, zamrdané!");

        private final int soundId;
        private final String message;

        private Sound(int soundId, String message) {
            this.soundId = soundId;
            this.message = message;
        }

        public int getSoundId() {
            return soundId;
        }

        public String getMessage() {
            return message;
        }

        public static List<String> getMessages() {
            List<String> messages = new ArrayList<String>();
            for (Sound sound : Sound.values()) {
                messages.add(sound.getMessage());
            }
            return messages;
        }
    }