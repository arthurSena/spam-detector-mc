package detector;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.IB1;
import weka.classifiers.lazy.KStar;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
public class Classificador {
    public static void main(String[] args) throws Exception {
    	System.out.println("s�klfjdaskldjfklasjdfklasjdfklasjdkflaskldfjaskldfj");
        //------------------------------------------------------
        // (1) importação da base de dados de treinamento
        //------------------------------------------------------
    	boolean verbose = false;
        if(args.length <3 ){
        	System.out.println("Entrada invalida !!!");
        }
    	else if(args[0].isEmpty()|| args[1].isEmpty()|| args[2].isEmpty()){
    		System.out.println("Entrada inv�lida !!!");
    	}
    	else{
    		if(args[0].equalsIgnoreCase("-v")){
    			verbose = true;
    		}
    		try{
    			DataSource source;
    			if(verbose){
    				source = new DataSource(args[2]);
    			}
    			else{
    				source = new DataSource(args[1]);
    			}
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
            
            KStar kstar = new KStar();
            kstar.buildClassifier(D);
            
            IB1 ib1 = new IB1();
            ib1.buildClassifier(D);
            
           //------------------------------------------------------
           // (3) Classificando um novo exemplo
           //------------------------------------------------------
            
            String[] palavras = new String[]{"make","address","all","3d","our","over","remove","internet",
        			"order","mail","receive","will","people","report","addresses","free","business","email",
        			"you","credit","your","font","000","money","hp","hpl","george","650","lab","labs","telnet",
        			"857","data","415","85","technology","1999", "parts","pm","direct","cs","meeting","original",
        			"project","re","edu","table","conference",";","(","[","!","$","#", "capital_run_length_average",
        			"capital_run_length_longest", "capital_run_length_total"};
            
            File arquivos[];  
//            File diretorio = new File("C:/Users/User/Desktop/Projetos Java/SpamDetector/enron2/ham"); 
            File diretorio;
            if(verbose){
            	 diretorio = new File(args[3]);  
            }
            else{
            	 diretorio = new File(args[2]);  
            }
            arquivos = diretorio.listFiles();  
            
            for(int i=0;i<arquivos.length;i++){
           	 try {  
                    FileReader reader = new FileReader(arquivos[i]);  
                    BufferedReader input = new BufferedReader(reader);  
                    String linha;  
                    String email = "";
                    while ((linha = input.readLine()) != null) {  
                      email +=linha+"\n";
                    }  
                    input.close();  
                    HashMap<String, Double> mapa = attributesScan(email);
                    Instance objeto = new Instance(58);
                    objeto.setDataset(D);
                    int cont = 0;
                    for (String z :palavras){
                   	 objeto.setValue(cont, mapa.get(z));
                   	 cont ++;
                    }
                    
                    if(args[0].equalsIgnoreCase("ibk") || args[1].equalsIgnoreCase("ibk")){
                    	if(verbose){
                    		if(k3.classifyInstance(objeto)==1){
                             	 System.out.println(arquivos[i].getName() + ": " + "spam");
                              }
                              else{
                             	 System.out.println(arquivos[i].getName() + ": " + "ham");
                              }
                            System.out.println("---------------------------------------------------------");
                    	}
                    }
                    else if(args[0].equalsIgnoreCase("kstar") || args[1].equalsIgnoreCase("kstar")){
                    	if(verbose){
                    	if(kstar.classifyInstance(objeto)==1){
                          	 System.out.println(arquivos[i].getName() + ": " + "spam");
                           }
                           else{
                          	 System.out.println(arquivos[i].getName() + ": " + "ham");
                           }
                           System.out.println("---------------------------------------------------------");}
                    }
                    else if(args[0].equalsIgnoreCase("ib1") || args[1].equalsIgnoreCase("ib1")){
                    	if(verbose){
                    	if(ib1.classifyInstance(objeto)==1){
                          	 System.out.println(arquivos[i].getName() + ": " + "spam");
                           }
                           else{
                          	 System.out.println(arquivos[i].getName() + ": " + "ham");
                           }
                           System.out.println("---------------------------------------------------------");}
                    }else{
                    	System.out.println("Digite um classificador valido !!!");
                    }
                  } catch (IOException ioe) {  
                     System.out.println("N�o foi poss�vel ler o arquivo: " + arquivos[1].getName());  
                  }  
            }
    		}
    		catch (Exception e) {
				System.out.println("Verifique o diretorio !!!");
			}
    	}
    	
    	
         
         
         
        
         
         // 3.1 criação de uma nova instância
//         Instance newInst = new Instance(5);
//         newInst.setDataset(D);
//         newInst.setValue(0, "rainy");
//         newInst.setValue(1, 71);
//         newInst.setValue(2, 79);
//         newInst.setValue(3, "TRUE");
//         // 3.2 classificação de uma nova instância
//         double pred = k3.classifyInstance(newInst);
//         
// 
//         // 3.3 imprime o valor de pred
//         System.out.println("Predi��o: " + pred);
//         System.out.println();
         
    }
    /**
     * Metodo que retorna a PORCENTAGEM de ocorrencias dos caracteres listados, em forma de um HashMap
     * @param mensagem Mensagem a ser Analizada
     * @return HashMap com as chaves ( palavras ) e valores ( porcentagem da ocorrencia da palavra )
     */
    private static HashMap<String, Double> attributesScan(String mensagem){
    	HashMap<String, Double> mapaOcorrencias = new HashMap<String, Double>();
    	
    	String[] palavras = new String[]{"make","address","all","3d","our","over","remove","internet",
    			"order","mail","receive","will","people","report","addresses","free","business","email",
    			"you","credit","your","font","000","money","hp","hpl","george","650","lab","labs","telnet",
    			"857","data","415","85","technology","1999", "parts","pm","direct","cs","meeting","original",
    			"project","re","edu","table","conference",";","(","[","!","$","#", "capital_run_length_average",
    			"capital_run_length_longest", "capital_run_length_total"};
    	
    	for(int i = 0; i < palavras.length; i++){
    		mapaOcorrencias.put(palavras[i], 0.0);
    	}
    	
    	String[] aux = mensagem.split(" ");
    	Object[] chaves = mapaOcorrencias.keySet().toArray();
    	
    	
    	for(int i = 0; i < chaves.length; i++){
    		
    		for(int j = 0; j < aux.length; j++){
    			if(aux[j].contains(chaves[i].toString())){
    				mapaOcorrencias.put(chaves[i].toString(), mapaOcorrencias.get(chaves[i].toString()) + 1.0);
    			}
    		}
    	}
    	
    	
    	for(int i = 0; i < chaves.length; i++){
    		mapaOcorrencias.put(chaves[i].toString(), (mapaOcorrencias.get(chaves[i].toString()) / aux.length) * 100.0 );
    	}
    	
    	
    	mapaOcorrencias.put("capital_run_length_average", capital_run_length_average(mensagem));
    	mapaOcorrencias.put("capital_run_length_longest", capital_run_length_longest(mensagem));
    	mapaOcorrencias.put("capital_run_length_total", capital_run_length_total(mensagem));
    	
    	
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
    private static double capital_run_length_longest(String mensagem){
    	String[] aux = mensagem.split(" ");
    	double maiorPalavraMaiuscula = 0.0;
    	
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
    private static double capital_run_length_total(String mensagem){
    	String[] aux = mensagem.split(" ");
    	double contLetrasMaiusculas = 0;
    	for(int i = 0; i < aux.length; i++){
    		String auxString = aux[i].trim();
    		if(auxString.toUpperCase().equals(auxString)){
    			contLetrasMaiusculas = contLetrasMaiusculas + auxString.length();
    		}
    	}
    	return contLetrasMaiusculas;
    }
    
}