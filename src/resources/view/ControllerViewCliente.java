package resources.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.entity.Cliente;
import main.util.MaskFieldUtil;
import main.util.ShowAlert;

public class ControllerViewCliente implements Initializable{

    @FXML
    private TableView<Cliente> Tabela;

    @FXML
    private TableColumn<Cliente, Integer> Id;

    @FXML
    private TableColumn<Cliente, String> Nome;

    @FXML
    private TableColumn<Cliente, String> Cpf;

    @FXML
    private TableColumn<Cliente, String> Email;

    @FXML
    private Label LabelChange;
    
    @FXML
    private Label LabelCPF;

    @FXML
    private Label LabelEmail;
    
    @FXML
    private Label LabelNome;
    
    @FXML
    private TextField TxtNome;

    @FXML
    private TextField TxtEmail;

    @FXML
    private TextField TxtCPF;

    @FXML
    private Button btnAdd;
    
    @FXML
    private Pane Painel;
    
    private String EscolhaAcao;
    
    private int IdCliente;
    
    private List<Cliente> listaClientes = new ArrayList<Cliente>();		

    @FXML
    void sair(ActionEvent event) {
    	new ShowAlert().mensagemAlert();
    	
        Stage stage = (Stage) btnAdd.getScene().getWindow(); 
	    stage.close();
    }

    @FXML
    void selecionarAcao(ActionEvent event) {
    	
    	String[] acoes = {"Incluir","Alterar","Excluir"};
    	
    	ChoiceDialog choiceDialog = new ChoiceDialog(acoes[0],acoes);
    	choiceDialog.setHeaderText("Selecionar Ação");
    	choiceDialog.setContentText("Por favor selecione uma ação:");
    	choiceDialog.setTitle("Selecionar Ação");
    	
    	Optional<String> resultado = choiceDialog.showAndWait();
    	
    	if(resultado.isPresent()) {
    		EscolhaAcao = choiceDialog.getSelectedItem().toString();
    		Painel.setVisible(false);
    		
    		if(EscolhaAcao.equals("Incluir")) {
    			
    			LabelChange.setText("Incluir Cliente");
    			
        		Painel.setVisible(true);
        		
    		} else if(EscolhaAcao.equals("Alterar")) {
    			
    			LabelChange.setText("Alterar Cliente");
    			
    			IdCliente = showTextInputDialog();
    		
        		if(!findById()) {
        			new ShowAlert().erroAlert();
        		} 
        		
    		} else {
    			
    			LabelChange.setText("Excluir Cliente");
    			
    			IdCliente = showTextInputDialog();
    		
        		if(!findById()) {
        			new ShowAlert().erroAlert();
        		} 
        		
    		}
    		
    	}
    	
    }
    
    public int showTextInputDialog() {
    	 TextInputDialog textInputDialog = new TextInputDialog(); 
    	  
    	 textInputDialog.setTitle("Selecionar Id");
    	 textInputDialog.setHeaderText("Entre com o Id");  
    	 
     	 Optional<String> resultado = textInputDialog.showAndWait();

     	if(resultado.isPresent()) {
     		return Integer.parseInt(textInputDialog.getEditor().getText());
     	}

     	return 0;
    }
    
    public boolean findById() {
        ObservableList<Cliente> lista = FXCollections.observableArrayList(listaClientes);
        
        for(Cliente cliente: lista) {
        	if(cliente.getId().equals(IdCliente)) {
        		Painel.setVisible(true);
        		TxtNome.setText(cliente.getNome());
        		TxtCPF.setText(cliente.getCpf());
        		TxtEmail.setText(cliente.getEmail());
        		return true;
        	}
        }
        return false;
    }
    
    @FXML
    void botaoAcao(ActionEvent event) {
    	if(EscolhaAcao.equals("Incluir")) {
    		incluirCliente();
    	} else if(EscolhaAcao.equals("Alterar")) {
    		alterarCliente();
    	} else {
    		excluirCliente();
    	}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Painel.setVisible(false);
		
		Nome.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Nome"));
		Cpf.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Cpf"));
		Email.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Email"));
		Id.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("Id"));
		
	    Tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		MaskFieldUtil.cpfField(this.TxtCPF);
	}
	
	public void incluirCliente() {
		
		if(validacaoCampos()) {
			
			listaClientes.add(new Cliente(listaClientes.size() + 1, TxtCPF.getText(), TxtEmail.getText(), TxtNome.getText()));
			
			new ShowAlert().sucessoAlert("Cliente adicionado com sucesso!");
			
			limparCampos();
			listar();
			
		} else {
			new ShowAlert().validacaoAlert();
		}
	}
	
	public void alterarCliente() {
		
		if(validacaoCampos()) {
			
	        ObservableList<Cliente> lista = FXCollections.observableArrayList(listaClientes);
	
	        for(Cliente cliente: lista) {
	        	if(cliente.getId().equals(IdCliente)) {
	        		listaClientes.remove(cliente);
	        		listaClientes.add(new Cliente(IdCliente, TxtCPF.getText(), TxtEmail.getText(), TxtNome.getText()));
	        	}
	        }
	        
			new ShowAlert().sucessoAlert("Cliente editado com sucesso!");

	    	Painel.setVisible(false);;
	    	
	        listar();
		} else {
			new ShowAlert().validacaoAlert();
		}
	}
	
	public void excluirCliente() {
		
        ObservableList<Cliente> lista = FXCollections.observableArrayList(listaClientes);

        if (new ShowAlert().confirmationAlert()) {
			for(Cliente cliente: lista) {
				if(cliente.getId().equals(IdCliente)) {
					listaClientes.remove(cliente);
		        }
		    }		
		}
        
    	limparCampos();
    	
        listar();
        
        Painel.setVisible(false);
	}
	
	
	public void listar() {
	    ObservableList<Cliente> lista = FXCollections.observableArrayList(listaClientes);

	    Tabela.setItems(lista);
	}
	
	public void limparCampos() {
		TxtNome.setText("");
		TxtCPF.setText("");
		TxtEmail.setText("");
	}

	public boolean validacaoCampos() {
		
		if(TxtEmail.getText().isEmpty() | TxtNome.getText().isEmpty() | TxtCPF.getText().isEmpty()) {
			return false;
		}	
		return true;
	}

}
