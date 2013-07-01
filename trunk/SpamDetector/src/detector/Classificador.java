package detector;



import java.util.HashMap;

import weka.core.converters.ConverterUtils.DataSource;

import weka.core.Instances;
import weka.core.Instance;
import weka.classifiers.lazy.IBk;
public class Classificador {
    public static void main(String[] args) throws Exception {
        //------------------------------------------------------
        // (1) importação da base de dados de treinamento
        //------------------------------------------------------
         DataSource source = new DataSource("weather.arff");
         Instances D = source.getDataSet();
         
         // 1.1 - espeficicação do atributo classe
         if (D.classIndex() == -1) {
             D.setClassIndex(D.numAttributes() - 1);
         }
        //------------------------------------------------------
        // (2) Construção do modelo classificador (treinamento)
        //------------------------------------------------------
         IBk k3 = new IBk(3);
         k3.buildClassifier(D);
         
         
        //------------------------------------------------------
        // (3) Classificando um novo exemplo
        //------------------------------------------------------
         
         // 3.1 criação de uma nova instância
         Instance newInst = new Instance(5);
         newInst.setDataset(D);
         newInst.setValue(0, "rainy");
         newInst.setValue(1, 71);
         newInst.setValue(2, 79);
         newInst.setValue(3, "TRUE");
         // 3.2 classificação de uma nova instância
         double pred = k3.classifyInstance(newInst);
         
 
         // 3.3 imprime o valor de pred
         System.out.println("Predição: " + pred);
         
    }
    /**
     * Metodo que retorna a QUANTIDADE de ocorrencias dos caracteres listados, em forma de um HashMap
     * @param mensagem Mensagem a ser Analizada
     * @return HashMap com as chaves ( palavras ) e valores ( numero de ocorrencia da palavra )
     */
    private static HashMap<String, Integer> attributesScan(String mensagem){
    	HashMap<String, Integer> mapaOcorrencias = new HashMap<String, Integer>();
    	
    	String[] palavras = new String[]{"make","address","all","3d","our","over","remove","internet",
    			"order","mail","receive","will","people","report","addresses","free","business","email",
    			"you","credit","your","font","000","money","hp","hpl","george","650","lab","labs","telnet",
    			"857","data","415","85","technology","1999", "parts","pm","direct","cs","meeting","original",
    			"project","re","edu","table","conference",";","(","[","!","$"};
    	
    	for(int i = 0; i < palavras.length; i++){
    		mapaOcorrencias.put(palavras[i], 0);
    	}
    	
    	String[] aux = mensagem.split(" ");
    	Object[] chaves = mapaOcorrencias.keySet().toArray();
    	
    	
    	for(int i = 0; i < chaves.length; i++){
    		
    		for(int j = 0; j < aux.length; j++){
    			if(aux[j].contains(chaves[i].toString())){
    				mapaOcorrencias.put(chaves[i].toString(), mapaOcorrencias.get(chaves[i].toString()) + 1);
    			}
    		}
    	}
    	
    	
		return mapaOcorrencias;
    	
    }
    /**
     * Metodo que retorna a media de letras maiusculas da mensagem
     * @param mensagem Mensagem a ser Analizada
     * @return Numero medio de letras maiusculas na mensagem
     */
    private static double capital_run_length_average(String mensagem){
    	String[] aux = mensagem.split(" ");
    	double contLetrasMaiusculas = 0;
    	double contPalavrasMaiusculas = 0;
    	for(int i = 0; i < aux.length; i++){
    		String auxString = aux[i].trim();
    		if(auxString.toUpperCase().equals(auxString)){
    			contLetrasMaiusculas = contLetrasMaiusculas + auxString.length();
    			contPalavrasMaiusculas++;
    		}
    	}
    	
    	double media = contLetrasMaiusculas / contPalavrasMaiusculas;
    	return media;
    	
    }
    /**
     * Metodo que retorna o tamanho da maior palavra maiuscula na mensagem
     * @param mensagem Mensagem a ser Analizada
     * @return Tamanho da maior palavra totalmente capitalizada
     */
    private static int capital_run_length_longest(String mensagem){
    	String[] aux = mensagem.split(" ");
    	int maiorPalavraMaiuscula = 0;
    	
    	for(int i = 0; i < aux.length; i++){
    		String auxString = aux[i].trim();
    		if(auxString.toUpperCase().equals(auxString)){
    			if(auxString.length() > maiorPalavraMaiuscula){
    				maiorPalavraMaiuscula = auxString.length();
    			}
    			
    		}
    	}
    	
    	return maiorPalavraMaiuscula;
    	
    }
    /**
     * Metodo que retorna o total de letras maiusculas de palavras totalmente capitalizadas
     * @param mensagem Mensagem a ser Analizada
     * @return Tamanho total das palavras totalmente capitalizadas
     */
    private static int capital_run_length_total(String mensagem){
    	String[] aux = mensagem.split(" ");
    	int contLetrasMaiusculas = 0;
    	for(int i = 0; i < aux.length; i++){
    		String auxString = aux[i].trim();
    		if(auxString.toUpperCase().equals(auxString)){
    			contLetrasMaiusculas = contLetrasMaiusculas + auxString.length();
    		}
    	}
    	return contLetrasMaiusculas;
    }
    
}