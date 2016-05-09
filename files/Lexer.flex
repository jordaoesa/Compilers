/**
 * Especificacao Lexica
 * @author jordaoesa
 */
package compiler.analysis.generated;

import java_cup.runtime.*;

%%

%class Lexer
%public
%unicode
%cup
%line
%column

%{
  public static int yyPreviousPublicLine;
  public static int yyPublicLine;
  StringBuffer string = new StringBuffer();
  
  public static int getCurrentLine(){
	if(Lexer.yyPreviousPublicLine != Lexer.yyPublicLine) return Lexer.yyPreviousPublicLine;
	return Lexer.yyPublicLine;
  }

  private Symbol symbol(int type) {
    yyPreviousPublicLine = yyPublicLine;
  	yyPublicLine = yyline+1;
    return new Symbol(type, yyline+1, yycolumn+1);
  }
  private Symbol symbol(int type, Object value) {
  	yyPreviousPublicLine = yyPublicLine;
  	yyPublicLine = yyline+1;
    return new Symbol(type, yyline+1, yycolumn+1, value);
  }
  private void reportError(int line, String message) {
    throw new RuntimeException("Erro lexico na linha " + line + ": " + message);
  }
  private int yywrap(){
    return 1;
  }
  private Symbol addIdentifierType(String identifier){
    if(identifier.contains("\'")) return new Symbol(sym.IDENTIFIER, sym.CHAR);
    return new Symbol(sym.IDENTIFIER, identifier); 
  }
%}


O	=   [0-7]
D	=   [0-9]
NZ	=	[1-9]
L	=   [a-zA-Z_]
A	=   [a-zA-Z_0-9]
H	=   [a-fA-F0-9]
HP	=	(0[xX])
E	=	([Ee][+-]?{D}+)
P	=	([Pp][+-]?{D}+)
FS	=	(f|F|l|L)
IS	=	(((u|U)(l|L|ll|LL)?)|((l|L|ll|LL)(u|U)?))
WS	=	[ \t\n\f\r]
CM	=	"/*" [^*] ~"*/" | "/*" "*"+ "/" | "//".*

IDE	=	{L}{A}*

CP	=	(u|U|L)
SP	=	(u8|u|U|L)
ES	=	(\\([\'\"\?\\nrtfabv]|{O}{1,3}|x{H}+))

%%

<YYINITIAL> {
	
	"include"				{ return symbol(sym.INCLUDE, yytext()); }
	"auto"					{ return symbol(sym.AUTO, yytext()); }
	"break"					{ return symbol(sym.BREAK, yytext()); }
	"case"					{ return symbol(sym.CASE, yytext()); }
	"char"					{ return symbol(sym.CHAR, yytext()); }
	"const"					{ return symbol(sym.CONST, yytext()); }
	"continue"				{ return symbol(sym.CONTINUE, yytext()); }
	"default"				{ return symbol(sym.DEFAULT, yytext()); }
	"do"					{ return symbol(sym.DO, yytext()); }
	"double"				{ return symbol(sym.DOUBLE, yytext()); }
	"else"					{ return symbol(sym.ELSE, yytext()); }
	"enum"					{ return symbol(sym.ENUM, yytext()); }
	"extern"				{ return symbol(sym.EXTERN, yytext()); }
	"float"					{ return symbol(sym.FLOAT, yytext()); }
	"for"					{ return symbol(sym.FOR, yytext()); }
	"goto"					{ return symbol(sym.GOTO, yytext()); }
	"if"					{ return symbol(sym.IF, yytext()); }
	"inline"				{ return symbol(sym.INLINE, yytext()); }
	"int"					{ return symbol(sym.INT, yytext()); }
	"long"					{ return symbol(sym.LONG, yytext()); }
	"register"				{ return symbol(sym.REGISTER, yytext()); }
	"restrict"				{ return symbol(sym.RESTRICT, yytext()); }
	"return"				{ return symbol(sym.RETURN, yytext()); }
	"short"					{ return symbol(sym.SHORT, yytext()); }
	"signed"				{ return symbol(sym.SIGNED, yytext()); }
	"sizeof"				{ return symbol(sym.SIZEOF, yytext()); }
	"static"				{ return symbol(sym.STATIC, yytext()); }
	"struct"				{ return symbol(sym.STRUCT, yytext()); }
	"switch"				{ return symbol(sym.SWITCH, yytext()); }
	"typedef"				{ return symbol(sym.TYPEDEF, yytext()); }
	"union"					{ return symbol(sym.UNION, yytext()); }
	"unsigned"				{ return symbol(sym.UNSIGNED, yytext()); }
	"void"					{ return symbol(sym.VOID, yytext()); }
	"volatile"				{ return symbol(sym.VOLATILE, yytext()); }
	"while"					{ return symbol(sym.WHILE, yytext()); }
	"_Alignas"              { return symbol(sym.ALIGNAS, yytext()); }
	"_Alignof"              { return symbol(sym.ALIGNOF, yytext()); }
	"_Atomic"               { return symbol(sym.ATOMIC, yytext()); }
	"_Bool"                 { return symbol(sym.BOOL, "bool"); }
	"_Complex"              { return symbol(sym.COMPLEX, yytext()); }
	"_Generic"              { return symbol(sym.GENERIC, yytext()); }
	"_Imaginary"            { return symbol(sym.IMAGINARY, yytext()); }
	"_Noreturn"             { return symbol(sym.NORETURN, yytext()); }
	"_Static_assert"        { return symbol(sym.STATIC_ASSERT, yytext()); }
	"_Thread_local"         { return symbol(sym.THREAD_LOCAL, yytext()); }
	"__func__"              { return symbol(sym.FUNC_NAME, yytext()); }
	
	{CM}					{ /* comentarios */ }
	
	
	/*{IDE}								{ return addIdentifierType(yytext()); }*/
	{IDE}								{ return symbol(sym.IDENTIFIER, yytext()); }
	
	{HP}{H}+{IS}?						{ return symbol(sym.I_CONSTANT, yytext()); }
	{NZ}{D}*{IS}?						{ return symbol(sym.I_CONSTANT, yytext()); }
	"0"{O}*{IS}?						{ return symbol(sym.I_CONSTANT, yytext()); }
	
	{CP}?"'"([^'\\\n]|{ES})+"'"			{ return symbol(sym.C_CONSTANT, yytext()); }
	
	{D}+{E}{FS}?						{ return symbol(sym.F_CONSTANT, yytext()); }
	{D}*"."{D}+{E}?{FS}?				{ return symbol(sym.F_CONSTANT, yytext()); }
	{D}+"."{E}?{FS}?					{ return symbol(sym.F_CONSTANT, yytext()); }
	{HP}{H}+{P}{FS}?					{ return symbol(sym.F_CONSTANT, yytext()); }
	{HP}{H}*"."{H}+{P}{FS}?				{ return symbol(sym.F_CONSTANT, yytext()); }
	{HP}{H}+"."{P}{FS}?					{ return symbol(sym.F_CONSTANT, yytext()); }
	
	({SP}?\"([^\"\\\n]|{ES})*\"{WS}*)+	{ return symbol(sym.STRING_LITERAL, yytext()); }
	
	"..."				{ return symbol(sym.ELLIPSIS); }
	">>="				{ return symbol(sym.RIGHT_ASSIGN); }
	"<<="				{ return symbol(sym.LEFT_ASSIGN); }
	"+="				{ return symbol(sym.ADD_ASSIGN); }
	"-="				{ return symbol(sym.SUB_ASSIGN); }
	"*="				{ return symbol(sym.MUL_ASSIGN); }
	"/="				{ return symbol(sym.DIV_ASSIGN); }
	"%="				{ return symbol(sym.MOD_ASSIGN); }
	"&="				{ return symbol(sym.AND_ASSIGN); }
	"^="				{ return symbol(sym.XOR_ASSIGN); }
	"|="				{ return symbol(sym.OR_ASSIGN); }
	">>"				{ return symbol(sym.RIGHT_OP); }
	"<<"				{ return symbol(sym.LEFT_OP); }
	"++"				{ return symbol(sym.INC_OP); }
	"--"				{ return symbol(sym.DEC_OP); }
	"->"				{ return symbol(sym.PTR_OP); }
	"&&"				{ return symbol(sym.AND_OP); }
	"||"				{ return symbol(sym.OR_OP); }
	"<="				{ return symbol(sym.LE_OP); }
	">="				{ return symbol(sym.GE_OP); }
	"=="				{ return symbol(sym.EQ_OP); }
	"!="				{ return symbol(sym.NE_OP); }
	
	/* novos coisinhas */
	
	";"					{ return symbol(sym.SEMICOLON); }
	("{"|"<%")			{ return symbol(sym.LEFT_BRACKET); }
	("}"|"%>")			{ return symbol(sym.RIGHT_BRACKET); }
	","					{ return symbol(sym.COMMA); }
	":"					{ return symbol(sym.COLON); }
	"="					{ return symbol(sym.ASSIGNMENT, yytext()); }
	"("					{ return symbol(sym.LEFT_PARENTESIS); }
	")"					{ return symbol(sym.RIGHT_PARENTESIS); }
	("["|"<:")			{ return symbol(sym.LEFT_SQ_BRACK); }
	("]"|":>")			{ return symbol(sym.RIGHT_SQ_BRACK); }
	"."					{ return symbol(sym.DOT); }
	"&"					{ return symbol(sym.AND_BINARY); }
	"!"					{ return symbol(sym.NEG); }
	"~"					{ return symbol(sym.NEG_BINARY); }
	"-"					{ return symbol(sym.MINUS); }
	"+"					{ return symbol(sym.PLUS); }
	"*"					{ return symbol(sym.TIMES); }
	"/"					{ return symbol(sym.DIV); }
	"%"					{ return symbol(sym.MOD); }
	"<"					{ return symbol(sym.LESS_THAN); }
	">"					{ return symbol(sym.GREATER_THAN); }
	"^"					{ return symbol(sym.XOR_BINARY); }
	"|"					{ return symbol(sym.OR_BINARY); }
	"?"					{ return symbol(sym.QUESTION); }
	"#"					{ return symbol(sym.POUND); }
	
	{WS}+				{ /* whitespace separates tokens */ }
	/*.					{ /* discard bad characters */ }*/
}

/* Entrada invalida */
[^] { reportError(yyline+1, "Caractere invalido \"" + yytext() + "\""); }