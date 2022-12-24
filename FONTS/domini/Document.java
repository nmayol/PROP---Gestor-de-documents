package domini;

import Exceptions.ExceptionAutorJaExisteix;
import Exceptions.ExceptionDirectoriNoEliminat;
import Exceptions.ExceptionFormatNoValid;
import Exceptions.ExceptionNotPrimaryKeys;
import utils.Format;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Document {
    private Format format;
    private String titol;
    private String autor;
    private HashMap<String, Double> ParaulaPesLocal;
    private ArrayList<String> contingut;

    double norma;

    public LocalDateTime getData() {
        return data;
    }

    LocalDateTime data;

    HashSet<String> stop_words;


    //CONSTRUCTORA
    /**
     * Creadora de la classe Document
     * @param Titol Titol del document
     * @param Autor Autor del document
     * @param Contingut Contingut del document
     * @param format format document
     * @throws ExceptionFormatNoValid salta quan el format no es valid
     * @throws ExceptionNotPrimaryKeys salta quan primary keys son incorrectes
     */
    public Document(Format format, String Titol, String Autor, ArrayList<String> Contingut) throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        if (format != Format.txt & format != Format.xml & format != Format.prop) throw new ExceptionFormatNoValid(format);
        if (Titol == null | Autor == null) throw new ExceptionNotPrimaryKeys(format, Titol, Autor);
        this.format = format;
        this.autor = Autor;
        this.titol = Titol;
        this.contingut = Contingut;
        this.ParaulaPesLocal = new HashMap<String, Double>();
        data = LocalDateTime.now();
        actualitzar_pesos(0);
    }

    //FUNCIONS
    /**
     * Modifica, de manera parcial o total, la informació d'un document
     * @param Titol Titol del document
     * @param Autor Autor del document
     * @param Contingut Contingut del document
     * @throws ExceptionNotPrimaryKeys Salta quan les claus primaries del document no són valides
     */
    public Document ModificarInformacioDocument(String Titol, String Autor, ArrayList<String> Contingut) throws ExceptionNotPrimaryKeys {
        if (Titol == null | Autor == null) throw new ExceptionNotPrimaryKeys(format, Titol, Autor);
        int word_antigues = this.contingut.size();
        this.contingut = Contingut;
        this.autor = Autor;
        this.titol = Titol;
        this.actualitzar_pesos(word_antigues);
        data = LocalDateTime.now();
        return this;
    }


    /**
     * Actualitza els pesos de la paraula dins del document
     * @param word_antigues num de paraules
     */
    public void actualitzar_pesos(int word_antigues){
        norma = 0;

        for (String word : ParaulaPesLocal.keySet()){
            double nou_pes = ParaulaPesLocal.get(word)*word_antigues;
            ParaulaPesLocal.replace(word, nou_pes);
        }

        for (int i = 0; i < getContingut().size(); ++i){
            if (!totes_stop_words().contains(getContingut().get(i))){
                if (!ParaulaPesLocal.containsKey(contingut.get(i))) ParaulaPesLocal.put(contingut.get(i), 1.0);
                else {
                    double nou_pes = ParaulaPesLocal.get(contingut.get(i));
                    ++nou_pes;
                    ParaulaPesLocal.replace(contingut.get(i), nou_pes);
                }
            }
        }
        for (String word : ParaulaPesLocal.keySet()){
            double nou_pes = ParaulaPesLocal.get(word) / contingut.size();
            norma += nou_pes*nou_pes;
            ParaulaPesLocal.replace(word, nou_pes);
        }
        norma = Math.sqrt(norma);
    }

    //GETTERS
    /**
     * Retorna el valor que té l'atribut Titol
     * @return retorna titol
     */
    public String getTitol(){ return titol;}
    /**
     * Retorna el valor que té l'atribut Autor
     * @return retorna autor
     */
    public String getAutor(){ return autor;}
    /**
     * Retorna el valor que té l'atribut Format
     * @return retorna format
     */
    public Format getFormat() {
        return format;
    }
    /**
     * Retorna el contingut del Document
     * @return retorna contingut
     */
    public ArrayList<String> getContingut(){return this.contingut;}
    /**
     * Retorna els pesos de les paraules dins del Document
     * @return retorna paraules i el seu pes local del document
     */
    public HashMap<String, Double> getPesosLocals(){
        return ParaulaPesLocal;
    }


    //SETTERS
    /**
     * Estableix el valor a l'atribut Titol
     * @param Titol titol nou
     */
    public void setTitol(String Titol){ this.titol = Titol;}
    /**
     * Estableix el valor a l'atribut Autor
     * @param Autor autor nou
     */
    public void setAutor(String Autor){this.autor = Autor;}
    /**
     * Estableix el valor a l'atribut Format
     * @param format1 format nou
     */
    public void setFormat(Format format1) {this.format = format1;}
    /**
     * Estableix un nou contingut al Document
     * @param Contingut contingut nou
     */
    public void setContingut(ArrayList<String> Contingut){this.contingut = Contingut;}

    public double getNorma() {
        return norma;
    }

    //STOP WORDS
    /**
     * Retorna les stopwords
     * @return retorna stopwords
     */
    public HashSet<String> totes_stop_words()  {
        stop_words = new HashSet<String>();
        String[] s = new String[]{"últim","última","últimes","últims","a","abans","això","al","algun","alguna","algunes","alguns",
                "alla","alli","allò","als","altra","altre","altres","amb","aprop","aqui","aquell","aquella","aquelles","aquells",
                "aquest","aquesta","aquestes","aquests","cada","catorze","cent","cert","certa","certes","certs","cinc","com","cosa"
                ,"d'","darrer","darrera","darreres","darrers","davant","de","del","dels","després","deu","dinou","disset","divuit",
                "dos","dotze","durant","el","ell","ella","elles","ells","els","en","encara","et","extra","fins","hi","i","jo","l'",
                "la","les","li","llur","lo","los","més","m'","ma","massa","mateix","mateixa","mateixes","mateixos","mes","meu","meva",
                "mig","molt","molta","moltes","molts","mon","mons","n'","na","ni","no","nosaltres","nostra","nostre","nou","ns","o","on",
                "onze","pel","per","però","perquè","perque","poc","poca","pocs","poques","primer","primera","primeres","primers","prop","què",
                "qual","quals","qualsevol","qualssevol","quan","quant","quanta","quantes","quants","quatre","que","qui","quin","quina","quines",
                "quins","quinze","res","s'","sa","segon","segona","segones","segons","sense","ses","set","setze","seu","seus","seva","seves","sino",
                "sis","sobre","son","sons","sota","t'","ta","tal","tals","tan","tant","tanta","tantes","tants","tes","teu","teus","teva","teves","ton",
                "tons","tot","tota","totes","tots","tres","tretze","tu","un","una","unes","uns","vint","vos","vosaltres","vosté","vostés","vostra",
                "vostre","vuit", "a","a's","able","about","above","according","accordingly","across","actually","after","afterwards","again","against",
                "ain't","all","allow","allows","almost","alone","along","already","also","although","always","am","among","amongst","an","and","another",
                "any","anybody","anyhow","anyone","anything","anyway","anyways","anywhere","apart","appear","appreciate","appropriate","are","aren't",
                "around","as","aside","ask","asking","associated","at","available","away","awfully","b","be","became","because","become","becomes",
                "becoming","been","before","beforehand","behind","being","believe","below","beside","besides","best","better","between","beyond","both",
                "brief","but","by","c","c'mon","c's","came","can","can't","cannot","cant","cause","causes","certain","certainly","changes","clearly","co",
                "com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could",
                "couldn't","course","currently","d","definitely","described","despite","did","didn't","different","do","does","doesn't","doing","don't",
                "done","down","downwards","during","e","each","edu","eg","eight","either","else","elsewhere","enough","entirely","especially","et","etc",
                "even","ever","every","everybody","everyone","everything","everywhere","ex","exactly","example","except","f","far","few","fifth","first",
                "five","followed","following","follows","for","former","formerly","forth","four","from","further","furthermore","g","get","gets","getting",
                "given","gives","go","goes","going","gone","got","gotten","greetings","h","had","hadn't","happens","hardly","has","hasn't","have",
                "haven't","having","he","he's","hello","help","hence","her","here","here's","hereafter","hereby","herein","hereupon","hers","herself",
                "hi","him","himself","his","hither","hopefully","how","howbeit","however","i","i'd","i'll","i'm","i've","ie","if","ignored","immediate",
                "in","inasmuch","inc","indeed","indicate","indicated","indicates","inner","insofar","instead","into","inward","is","isn't","it","it'd",
                "it'll","it's","its","itself","j","just","k","keep","keeps","kept","know","knows","known","l","last","lately","later","latter","latterly",
                "least","less","lest","let","let's","like","liked","likely","little","look","looking","looks","ltd","m","mainly","many","may","maybe",
                "me","mean","meanwhile","merely","might","more","moreover","most","mostly","much","must","my","myself","n","name","namely","nd","near",
                "nearly","necessary","need","needs","neither","never","nevertheless","new","next","nine","no","nobody","non","none","noone","nor",
                "normally","not","nothing","novel","now","nowhere","o","obviously","of","off","often","oh","ok","okay","old","on","once","one","ones",
                "only","onto","or","other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","own","p","particular",
                "particularly","per","perhaps","placed","please","plus","possible","presumably","probably","provides","q","que","quite","qv","r","rather",
                "rd","re","really","reasonably","regarding","regardless","regards","relatively","respectively","right","s","said","same","saw","say",
                "saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious",
                "seriously","seven","several","shall","she","should","shouldn't","since","six","so","some","somebody","somehow","someone","something",
                "sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure","t",
                "t's","take","taken","tell","tends","th","than","thank","thanks","thanx","that","that's","thats","the","their","theirs","them",
                "themselves","then","thence","there","there's","thereafter","thereby","therefore","therein","theres","thereupon","these","they",
                "they'd","they'll","they're","they've","think","third","this","thorough","thoroughly","those","though","three","through","throughout",
                "thru","thus","to","together","too","took","toward","towards","tried","tries","truly","try","trying","twice","two","u","un","under",
                "unfortunately","unless","unlikely","until","unto","up","upon","us","use","used","useful","uses","using","usually","uucp","v","value",
                "various","very","via","viz","vs","w","want","wants","was","wasn't","way","we","we'd","we'll","we're","we've","welcome","well","went",
                "were","weren't","what","what's","whatever","when","whence","whenever","where","where's","whereafter","whereas","whereby","wherein",
                "whereupon","wherever","whether","which","while","whither","who","who's","whoever","whole","whom","whose","why","will","willing","wish",
                "with","within","without","won't","wonder","would","would","wouldn't","x","y","yes","yet","you","you'd","you'll","you're","you've",
                "your","yours","yourself","yourselves","z", "a","actualmente","adelante","además","afirmó","agregó","ahora","ahi","al","algo","alguna","algunas","alguno","algunos"
                ,"algún","alrededor","ambos","ante","anterior","antes","apenas","aproximadamente","aqui","aseguró","asi","aunque","ayer","añadió","aún",
                "bajo","bien","buen","buena","buenas","bueno","buenos","cada","casi","cerca","cierto","cinco","comentó","como","con","conocer","considera"
                ,"consideró","contra","cosas","creo","cual","cuales","cualquier","cuando","cuanto","cuatro","cuenta","cómo","da","dado","dan","dar","de",
                "debe","deben","debido","decir","dejó","del","demás","dentro","desde","después","dice","dicen","dicho","dieron","diferente","diferentes",
                "dijeron","dijo","dio","donde","dos","durante","e","ejemplo","el","ella","ellas","ello","ellos","embargo","en","encuentra","entonces",
                "entre","era","eran","es","esa","esas","ese","eso","esos","esta","estaba","estaban","estamos","estar","estará","estas","este","esto",
                "estos","estoy","estuvo","está","están","ex","existe","existen","explicó","expresó","fin","fue","fuera","fueron","gran","grandes","ha",
                "haber","habrá","habia","habian","hace","hacen","hacer","hacerlo","hacia","haciendo","han","hasta","hay","haya","he","hecho","hemos",
                "hicieron","hizo","hoy","hubo","igual","incluso","indicó","informó","junto","la","lado","las","le","les","llegó","lleva","llevar","lo",
                "los","luego","lugar","manera","manifestó","mayor","me","mediante","mejor","mencionó","menos","mi","mientras","misma","mismas","mismo",
                "mismos","momento","mucha","muchas","mucho","muchos","muy","más","nada","nadie","ni","ninguna","ningunas","ninguno","ningunos","ningún",
                "no","nos","nosotras","nosotros","nuestra","nuestras","nuestro","nuestros","nueva","nuevas","nuevo","nuevos","nunca","o","ocho","otra",
                "otras","otro","otros","para","parece","parte","partir","pasada","pasado","pero","pesar","poca","pocas","poco","pocos","podemos","podrá",
                "podrán","podria","podrian","poner","por","porque","posible","primer","primera","primero","primeros","principalmente","propia","propias",
                "propio","propios","próximo","próximos","pudo","pueda","puede","pueden","pues","que","quedó","queremos","quien","quienes","quiere","quién",
                "qué","realizado","realizar","realizó","respecto","se","sea","sean","segunda","segundo","según","seis","ser","será","serán","seria",
                "señaló","si","sido","siempre","siendo","siete","sigue","siguiente","sin","sino","sobre","sola","solamente","solas","solo","solos","son",
                "su","sus","si","sólo","tal","también","tampoco","tan","tanto","tendrá","tendrán","tenemos","tener","tenga","tengo","tenido","tenia",
                "tercera","tiene","tienen","toda","todas","todavia","todo","todos","total","tras","trata","través","tres","tuvo","un","una","unas",
                "uno","unos","usted","va","vamos","van","varias","varios","veces","ver","vez","y","ya","yo","él","ésta","éstas","éste","éstos","última",
                "últimas","último","últimos"};
        stop_words.addAll(List.of(s));
        return stop_words;
    }
}