package com.omendra.graph2d;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Main extends JPanel {

    private JFrame f;
    private JLabel lb1,lb2,lb3;
    private JComboBox jcb;
    private JTextField txt;
    private JButton bt1,bt2,bt3;
    private int px,py;
    private double d1;
    private boolean fe;

    public Main() {
        super();
        fe = false;
        px = py = 0;
        f = new JFrame("Graph2D");
        f.setSize(800,690);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        this.setLayout(null);
        lb1 = new JLabel("Scale : 40 px = 1 div = 1 cm");
        lb1.setFont(new Font("Times New Romen",0,12));
        lb1.setBounds(10,610,150,20);
        add(lb1);
        lb2 = new JLabel("Valid Operators");
        lb2.setFont(new Font("Times New Romen",0,12));
        lb2.setBounds(10,630,100,20);
        add(lb2);
        lb3 = new JLabel("F(x) =");
        lb3.setFont(new Font("Times New Romen",0,12));
        lb3.setBounds(270,610,50,20);
        add(lb3);
        String ar[]={"x","e","(",")","/","*","+","-","^","sin(","cos(","tan(","cot(","sec(","cosec(","log(","ln("};
        jcb = new JComboBox(ar);
        jcb.setBounds(120,630,70,20);
        add(jcb);
        txt = new JTextField("");
        txt.setFont(new Font("Courier New",0,12));
        txt.setBounds(300,610,480,20);
        add(txt);
        bt1 = new JButton("Use");
        bt1.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ae){ bt1Task(); }});
        bt1.setBounds(200,630,60,20);
        add(bt1);
        bt2 = new JButton("Plot");
        bt2.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ae){ bt2Task(); }});
        bt2.setBounds(300,630,100,20);
        add(bt2);
        bt3 = new JButton("Reset");
        bt3.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent ae){ bt3Task(); }});
        bt3.setBounds(680,630,100,20);
        add(bt3);
        f.add(this);
        f.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.drawLine(400,0,400,600);
        g.drawLine(0,300,800,300);
        g.drawRect(0,0,800,600);
        for(int i = 0;i < 21;i++)
            g.drawLine(40*i,298,40 * i,302);
        for(int i = 0;i < 15;i++)
            g.drawLine(398,20 + i * 40,402,20 + i * 40);
    }

    void bt1Task() {
        txt.setText(txt.getText()+(String)jcb.getSelectedItem());
    }

    void bt2Task() {
        Graphics g = this.getGraphics();
        g.setColor(Color.red);
        String exp, copy;
        int i,j;
        exp = txt.getText();
        px = py = 0;
        exp = exp.replaceAll(" ","");
        exp = exp.replaceAll("cosec","F");
        exp = exp.replaceAll("sin","A");
        exp = exp.replaceAll("cos","B");
        exp = exp.replaceAll("tan","C");
        exp = exp.replaceAll("cot","D");
        exp = exp.replaceAll("sec","E");
        exp = exp.replaceAll("log","G");
        exp = exp.replaceAll("ln","H");
        exp = exp.replaceAll("e","2.718281828");
        for(i = -400;i <= 400;i++) {
            try {
                copy = exp;
                copy = copy.replaceAll("x",Double.toString(i / 40.0));
                j = (int)(40*Double.parseDouble(value(copy)));
                if(fe) {
                    fe = false;
                    throw new Exception();
                }
                if(j >= -300) {
                    if(j <= 300) {
                        g.drawLine(px,py,400 + i,300 - j);
                        px = 400 + i;
                        py = 300 - j;
                    }
                    else {
                        g.drawLine(px,py,400 + i,0);
                        px = 400 + i;
                        py = 0;
                    }
                }
                else {
                    g.drawLine(px,py,400 + i,600);
                    px = 400+i;
                    py = 600;
                }
            }
            catch(Exception e) {
                g.setColor(Color.yellow);
                g.drawLine(400+i,0,400+i,600);
                g.setColor(Color.red);
            }
        }
    }

    void bt3Task() {
        f.setVisible(false);
        txt.setText("");
        f.setVisible(true);
    }

    String value(String s) {
        String part,sub;
        char ch1,ch2;
        int index1,index2,count,i,a;
        double res = 0.0;
        for(i = 0;i < s.length()-3;i++) {
            ch1 = s.charAt(i);
            if(ch1 >= 65 && ch1 <= 90) {
                index1 = i;
                index2 = index1+2;
                count = 1;
                while(count != 0) {
                    ch2 = s.charAt(index2);
                    if(ch2 == '(')
                        count++;
                    else if(ch2 == ')')
                        count--;
                    index2++;
                }
                d1 = Double.parseDouble(value(s.substring(index1+2,index2-1)));
                switch (ch1) {
                    case 'A': d1 = Math.sin(d1); break;
                    case 'B': d1 = Math.cos(d1); break;
                    case 'C': d1 = Math.tan(d1); break;
                    case 'D': d1 = Math.tan(d1); if(d1 == 0) fe = true; d1 = 1.0/d1; break;
                    case 'E': d1 = Math.cos(d1); if(d1 == 0) fe = true; d1 = 1.0/d1; break;
                    case 'F': d1 = Math.sin(d1); if(d1 == 0) fe = true; d1 = 1.0/d1; break;
                    case 'G': if(d1 < 0) fe = true; if(d1 == 0) { px = 400; py = 600; } d1 = Math.log10(d1); break;
                    case 'H': if(d1 < 0) fe = true; if(d1 == 0) { px = 400; py = 600; } d1 = Math.log(d1); break;
                }
                d1 = ((long)(d1 * 1000)) / 1000.0;
                sub = Double.toString(d1);
                s = s.substring(0,index1) + sub + (index2 == s.length() ? "" : s.substring(index2));
                i = 0;
            }
            else if(ch1 == '(') {
                index1 = i;
                index2 = index1;
                count = 1;
                while(count != 0) {
                    index2++;
                    ch2 = s.charAt(index2);
                    if(ch2 == '(')
                        count++;
                    else if(ch2 == ')')
                        count--;
                }
                s = s.substring(0,index1) + value(s.substring(index1+1,index2)) + (index2 == s.length()-1 ? "" : s.substring(index2+1));
                i = 0;
            }
        }
        for(i = 0;i < 3;i++) {
            ch2 = switch (i) {
                case 0 -> '^';
                case 1 -> '/';
                case 2 -> '*';
                default -> ' ';
            };
            index1 = s.indexOf(ch2);
            while(index1 != -1) {
                index2 = index1;
                do {
                    index2--;
                    ch1=s.charAt(index2);
                    if(index2 == 0) {
                        index2--;
                        break;
                    }
                } while(ch1 != '+' && ch1 != '-' && ch1 != '*' && ch1 != '/');
                a = index2+1;
                sub = s.substring(a,index1);
                index2 = index1+1;
                ch1 = s.charAt(index2);
                if(ch1 == '-')
                    index2++;
                do {
                    index2++;
                    if(index2 == s.length())
                        break;
                    ch1 = s.charAt(index2);
                } while(ch1 != '+' && ch1 != '-' && ch1 != '*' && ch1 != '/');
                double b = Double.parseDouble(s.substring(index1 + 1, index2));
                switch (i) {
                    case 0 -> res = Math.pow(Double.parseDouble(sub), b);
                    case 1 -> res = Double.parseDouble(sub) / b;
                    case 2 -> res = Double.parseDouble(sub) * b;
                }
                res = ((long)(res * 1000)) / 1000.0;
                s = s.substring(0,a) + Double.toString(res) + (index2 == s.length() ? "" : s.substring(index2));
                index1 = s.indexOf(ch2);

            }
        }
        return Double.toString(adder(s));
    }

    double adder(String s) {
        if(s.length() == 0)
            return 0;
        int p = s.indexOf('+');
        if(p >= 0)
            return (adder(s.substring(0,p)) + adder(s.substring(p+1)));
        p = s.indexOf('-');
        if(p >= 0) {
            String s1 = s.substring(0,p);
            String s2 = s.substring(p+1);
            s2 = s2.replaceAll("-","+");
            return (adder(s1)-adder(s2));
        }
        return Double.parseDouble(s);
    }

    public static void main(String args[])throws IOException {
        Main g2d = new Main();
    }
}