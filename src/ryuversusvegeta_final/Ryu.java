package ryuversusvegeta_final;

import javaPlay.Keys;
import javaPlay.Imagem;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javaPlay.GameEngine;
import javaPlay.GameObject;
import javaPlay.Keyboard;
import javaPlay.Sprite;
import javaPlayExtras.ObjetoComGravidade;
import javax.swing.JOptionPane;

public class Ryu extends ObjetoComGravidade {

    protected Imagem imgNormal;
    protected Imagem imgTras;
    protected Imagem imgFrente;
    protected Imagem imgPulo;
    protected Imagem imgApanhando;
    protected Imagem imgSoco;
    protected Imagem imgChute;

    //Esta � a imagem que sempre ser� desenhada e utilizada para colis�o
    //Ela � uma refer�ncia para uma das imagens acima.
    protected Imagem imgAtual;

    protected int velocidade = 10;
    protected int vida = 100;

    protected EstadoPersonagem estado;
    protected int forcaPulo = 38;
    
    //For�a imagem do jogador apanhando a permanecer por 5 frames
    //Veja m�todo step para detalhes
    protected int contadorApanhando = 0;    

    public Ryu() {
        try {
            this.imgNormal = new Imagem("resources/ryu/normal.png");
            this.imgTras = new Imagem("resources/ryu/normal.png");
            this.imgFrente = new Imagem("resources/ryu/normal.png");
            this.imgPulo = new Imagem("resources/ryu/pulo.png");
            this.imgSoco = new Imagem("resources/ryu/soco_esquerda.png");
            this.imgChute = new Imagem("resources/ryu/chute.png");
            this.imgApanhando = new Imagem("resources/ryu/apanhando.png");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Exce��o: "+ex.getMessage());
            System.exit(0);
        }

        this.imgAtual = this.imgNormal;
        this.x = 400;

        //Essencial par o objetoComGravidade poder trabalhar
        this.setAltura( this.imgAtual.pegaAltura() );
        this.setLargura( this.imgAtual.pegaLargura() );
    }

    public void step(long timeElapsed) {
        //Executa step da classe ObjetoComMovimento
        super.step(timeElapsed);
        
        //Se o jogador levou um golpe, 
        // o contador inicia em zero..
        //enquanto n�o passarem 15 frames, 
        //o comando do jogador fica travado.        
        if(this.contadorApanhando > 5){
            this.estado = EstadoPersonagem.NORMAL;            
        } else {
            this.contadorApanhando++;            
        }

        Keyboard keyboard = GameEngine.getInstance().getKeyboard();

        if( keyboard.keyDown(Keys.D) ){
          this.frente();
        } else if( keyboard.keyDown(Keys.A) ){
          this.tras();
        } else if( keyboard.keyDown(Keys.W) ){
          this.soco();
        } else if( keyboard.keyDown(Keys.E) ){
          this.chute();
        } else if( keyboard.keyDown(Keys.S)){
          this.pula();
        }else {
          this.normal();
        }                
        
    }


    public void alteraImagem(Imagem novaImagem){
        this.imgAtual = novaImagem;
        this.setAltura( this.imgAtual.pegaAltura() );
        this.setLargura( this.imgAtual.pegaLargura() );
    }

    public void normal() {
        
        if(this.estado == EstadoPersonagem.APANHANDO ){
            this.alteraImagem( this.imgApanhando );
        } else {
            this.estado = EstadoPersonagem.NORMAL;
            this.alteraImagem( this.imgNormal );
        }
    }

    public void pula() {        
        if(this.estaSubindo() || this.estaDescendo()){
            return;
        }

        this.alteraImagem(this.imgPulo);
        this.impulso(this.forcaPulo);
    }

    public void soco() {
        this.alteraImagem(this.imgSoco);
        this.estado = EstadoPersonagem.SOCO;
    }

    public void chute() {
        this.alteraImagem(this.imgChute);
        this.estado = EstadoPersonagem.CHUTE;
    }

    public void frente() {
        this.alteraImagem(this.imgFrente);
        this.x += this.velocidade;        
    }

    public void tras() {
        this.alteraImagem(this.imgTras);
        this.x -= this.velocidade;
    }
    
    public void levaSoco(){
        //Se j� est� apanhando, n�o pode levar dois golpes seguidos.
        if(this.estado == EstadoPersonagem.APANHANDO){
            return;
        }
        this.vida -= 10;
        this.x += 15;
        this.estado = EstadoPersonagem.APANHANDO;
        this.alteraImagem(this.imgApanhando);
        this.contadorApanhando = 0;               
    }

    public void diminuiVida(int numPontos) {
        this.vida -= numPontos;
    }
   
    public boolean estaMorto() {
        return (this.vida <= 0);
    }


    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.drawString(this.vida + "", this.x + 20, this.y - 10);

        g.drawRect(this.x, this.y, this.imgAtual.pegaLargura(), this.imgAtual.pegaAltura());
        
        this.imgAtual.drawFlipped(g, this.x, this.y);
    }
}

