package detector;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.IB1;
import weka.classifiers.lazy.KStar;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
public class Classificador {
    public static void main(String[] args) throws Exception {

    	boolean verbose = false;
    	String out = "";
    	
    	//Definindo os valores da entrada
    	
        if(args.length <3 ){
        	System.out.println("Entrada invalida !!!");
        }
    	else if(args[0].isEmpty()|| args[1].isEmpty()|| args[2].isEmpty()){
    		System.out.println("Entrada inválida !!!");
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
               
            
            //espeficicao do atributo classe
    			
    		Instances D = source.getDataSet();
    		
            if (D.classIndex() == -1) {
                D.setClassIndex(D.numAttributes() - 1);
            }
            
           //Construcao do modelo classificador (treinamento)
          
            Classifier classificador = null;
            
         
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
            
            //Laco para imprimir os valores das analizes de cada email de acordo
            //com o algoritmo passado na entrada
            
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
                    
                    // O usuario escolheu o algoritmo IBK
                    if(args[0].equalsIgnoreCase("ibk") || args[1].equalsIgnoreCase("ibk")){
               
                    	classificador = new IBk(3);
                    	classificador.buildClassifier(D);
                    	if(verbose){
                    		if(classificador.classifyInstance(objeto)==1){
                             	 out = out + arquivos[i].getName() + ": " + "spam" + "\n";
                              }
                              else{
                             	 out = out + arquivos[i].getName() + ": " + "ham" + "\n";
                              }
                    		out = out + "---------------------------------------------------------" + "\n";
                    	}else{
                    		out = out + getStatistics(D, classificador);
                            break;
                    	}

                    }
                    
                 // O usuario escolheu o algoritmo Kstar
                    else if(args[0].equalsIgnoreCase("kstar") || args[1].equalsIgnoreCase("kstar")){
                    	classificador = new KStar();
                    	classificador.buildClassifier(D);
                    	if(verbose){
                    	if(classificador.classifyInstance(objeto)==1){
                    		 out = out + arquivos[i].getName() + ": " + "spam" + "\n";
                           }
                           else{
                        	   out = out + arquivos[i].getName() + ": " + "ham" + "\n";
                           }
                    	out = out + "---------------------------------------------------------" + "\n";}
                    	else{
                    		out = out + getStatistics(D, classificador);
                            break;
                    	}
                    	
                    }
                    
                 // O usuario escolheu o algoritmo IB1
                    else if(args[0].equalsIgnoreCase("ib1") || args[1].equalsIgnoreCase("ib1")){
                    	classificador = new IB1();
                    	classificador.buildClassifier(D);
                    	if(verbose){
                    	if(classificador.classifyInstance(objeto)==1){
                    		out = out + arquivos[i].getName() + ": " + "spam" ;
                           }
                           else{
                        	   out = out + arquivos[i].getName() + ": " + "ham";
                           }
                           out = out + "---------------------------------------------------------" + "\n";}
                    	else{
                    		out = out + getStatistics(D, classificador);
                            break;
                    	}
                    	
                    }else{
                    	System.out.println("Digite um classificador valido !!!");
                    }
                  } catch (IOException ioe) {  
                     System.out.println("Não foi possível ler o arquivo: " + arquivos[1].getName());  
                  }  
            	}
            	
            	if(verbose && classificador != null){
            		out = out + getStatistics(D, classificador);
            	}
            	
            	
            	saveOutFile(out);
            
    		}
    		catch (Exception e) {
				System.out.println("Verifique o diretorio !!!");
			}
    	}
    	
         
    }
    
    private static void saveOutFile(String outText){
    	
    	outText = outText + "\n\n" + new Date(System.currentTimeMillis());
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
    		out.write(outText);
    		out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    	
    }
    
    private static String getStatistics(Instances D, Classifier C) throws Exception{
    	Evaluation evaluation = new Evaluation(D);
        Random rand = new Random(1);
        evaluation.crossValidateModel(C, D, 10, rand);
        return "precision: " + evaluation.precision(0) + "\n" + "recall: " + evaluation.recall(0) + "\n" + "f-measure: " + evaluation.fMeasure(0);
    	
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