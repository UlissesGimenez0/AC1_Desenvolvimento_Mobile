package com.example.ac1;

/* ULISSES DA SILVA GIMENEZ - RA: 249615*/
/* KAIQUE FARIA ROMANI - RA: 247661 */

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtNome, edtEmail, edtTelefone, edtCidade ;
    Button btnSalvar;
    ListView listViewUsuarios;
    BancoHelper databaseHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> listaUsuarios;
    ArrayList<Integer> listaIds;
    Spinner spinnerCategoria;
    CheckBox checkboxFav;



    private void carregarUsuarios() {
        Cursor cursor = databaseHelper.listarContatos();
        listaUsuarios = new ArrayList<>();
        listaIds = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nome = cursor.getString(1);
                String email = cursor.getString(2);
                listaUsuarios.add(id + " - " + nome + " - " + email);
                listaIds.add(id);
            } while (cursor.moveToNext());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaUsuarios);
        listViewUsuarios.setAdapter(adapter);
    }


    ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(
            this, R.array.opcoes_spinner, android.R.layout.simple_spinner_item);

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
    private List<CheckBox> checkBoxList = new ArrayList<>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

  /*      String[] opcoes ={"Familia", "Amigos","Trabalho", "Outros"};

        for (String opcao : opcoes) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(opcao);
            checkBoxContainer.addView(checkBox);
            checkBoxList.add(checkBox);
        }*/



        try
        {
             edtNome = findViewById(R.id.edtNome);
             edtEmail = findViewById(R.id.edtEmail);
             edtTelefone = findViewById(R.id.edtTelefone);
             edtCidade = findViewById(R.id.edtCidade);
             spinnerCategoria = findViewById(R.id.spinnerCategoria);
             btnSalvar = findViewById(R.id.btnSalvar);
             checkboxFav = findViewById(R.id.checkboxFav);
             listViewUsuarios = findViewById(R.id.listViewUsuarios);
             databaseHelper = new BancoHelper(this);

            btnSalvar.setOnClickListener(v -> {

                String nome = edtNome.getText().toString();
                String email = edtEmail.getText().toString();
                String telefone = edtTelefone.getText().toString();
                String cidade = edtCidade.getText().toString();
                String categoria = spinnerCategoria.getSelectedItem().toString();
                String favorito = checkboxFav.getText().toString();

                if (!nome.isEmpty() && !email.isEmpty()) {
                    long resultado = databaseHelper.InserirContato(nome, email,telefone,cidade,favorito,categoria );
                    if (resultado != -1) {
                        Toast.makeText(this, "Usuário salvo!", Toast.LENGTH_SHORT).show();
                        edtNome.setText("");
                        edtEmail.setText("");
                        edtTelefone.setText("");
                        edtCidade.setText("");
                        spinnerCategoria.getSelectedItem();
                        checkboxFav.setText("");
                        carregarUsuarios();
                    } else {
                        Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            });

           listViewUsuarios.setOnItemClickListener((parent, view, position, id) -> {
                int userId = listaIds.get(position);
                String nome = listaUsuarios.get(position).split(" - ")[1];
                String email = listaUsuarios.get(position).split(" - ")[2];
                String telefone = listaUsuarios.get(position).split(" - ")[3];
                String cidade = listaUsuarios.get(position).split(" - ")[4];
                String categoria = listaUsuarios.get(position).split(" - ")[5];
                String favorito = listaUsuarios.get(position).split(" - ")[6];
                edtNome.setText(nome);
                edtEmail.setText(email);
                edtTelefone.setText(telefone);
                edtCidade.setText(cidade);
                spinnerCategoria.getSelectedItem().toString();
                checkboxFav.setText(favorito);

                btnSalvar.setText("Atualizar");

                btnSalvar.setOnClickListener(v ->
                {
                    String novoNome = edtNome.getText().toString();
                    String novoEmail = edtEmail.getText().toString();
                    String novoTelefone = edtTelefone.getText().toString();
                    String novoCategoria = String.valueOf(spinnerCategoria.getTextDirection());
                    String novoCidade = edtCidade.getText().toString();
                    String novoFavorito= checkboxFav.getText().toString();
                    if (!novoNome.isEmpty() && !novoEmail.isEmpty()) {
                        int resultado = databaseHelper.atualizarUsuario(userId, novoNome, novoEmail,novoTelefone,novoCategoria,novoCidade,novoFavorito);
                        if (resultado > 0) {
                            Toast.makeText(this, "Usuário atualizado!", Toast.LENGTH_SHORT).show();
                            carregarUsuarios();
                            edtNome.setText("");
                            edtEmail.setText("");
                            edtTelefone.setText("");
                            edtCidade.setText("");
                            checkboxFav.setText("");
                            spinnerCategoria.setTextDirection(Integer.parseInt(""));

                            btnSalvar.setText("Salvar");
                        } else {
                            Toast.makeText(this, "Erro ao atualizar!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                listViewUsuarios.setOnItemLongClickListener((adapterView, view1, pos, l) -> {
                    int idUsuario = listaIds.get(pos);
                    int deletado = databaseHelper.excluirUsuario(idUsuario);
                    if (deletado > 0) {
                        Toast.makeText(this, "Usuário excluído!", Toast.LENGTH_SHORT).show();
                        carregarUsuarios();
                    }
                    return true;
                });
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}