package utils;

import Exceptions.ExceptionConsultaLimitParametres;
import Exceptions.ExceptionFormatNoValid;
import Exceptions.ExceptionNotPrimaryKeys;
import domini.*;
import persistencia.accesObjects.*;

public class ConvertirAO {
    public DocumentAO convertirDoc(Document d){
        return new DocumentAO(d.getFormat(), d.getTitol(), d.getAutor(), d.getPesosLocals(), d.getContingut(), d.getNorma());
    }

    public Document convertirDocAO(DocumentAO d) throws ExceptionFormatNoValid, ExceptionNotPrimaryKeys {
        return new Document(d.getFormat(), d.getTitol(), d.getAutor(), d.getContingut());
    }

    public ConsultaAO convertirConsulta(Consulta c){
        return new ConsultaAO(c.getParametres(), c.getTipus());
    }

    public Consulta convertirConsultaAO(ConsultaAO c) throws ExceptionConsultaLimitParametres {
        return new Consulta(c.getParametres(), c.getTipusConsulta());
    }
    public IndexAutorAO convertirIA(IndexAutor ia){
        return new IndexAutorAO(ia.getIndexautor());
    }

    public IndexAutor convertirIA_AO(IndexAutorAO ia){
        return new IndexAutor(ia.getIndexAutor());
    }
    public IndexDocumentsAutorAO convertirIDA(IndexDocumentsAutor ida){
        return new IndexDocumentsAutorAO(ida.getAutorTitols());
    }

    public IndexDocumentsAutor convertirIDA_AO(IndexDocumentsAutorAO ida){
        return new IndexDocumentsAutor(ida.getAutorTitols());
    }

    public IndexExpressionsBooleanesAO convertirIEB(IndexExpressionsBooleanes ieb){
        return new IndexExpressionsBooleanesAO(ieb.getIndex());
    }

    public IndexExpressionsBooleanes convertirIEB_AO(IndexExpressionsBooleanesAO ieb){
        return new IndexExpressionsBooleanes(ieb.getIndex());
    }
}
