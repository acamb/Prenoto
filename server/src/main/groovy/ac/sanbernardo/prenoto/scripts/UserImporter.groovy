package ac.sanbernardo.prenoto.scripts
@Grab('com.h2database:h2:1.4.200')
@Grab('org.mindrot:jbcrypt:0.4')
@GrabConfig(systemClassLoader=true)

import groovy.sql.Sql
import org.mindrot.jbcrypt.BCrypt;

boolean SIMULATE = false
File csv = new File(args[0])
String user = args[1]
String password = args[2]
String url = args[3]

Sql sql = Sql.newInstance(url,user,password,'org.h2.Driver')
csv.splitEachLine(";") { line ->
    try {
        Long id = line[0].toLong()
        String nome = line[2]
        String cognome = line[1]
        String cfisc = line[4]

        String userPassword = BCrypt.hashpw(cfisc.toUpperCase(), BCrypt.gensalt(10))
        def query
        if (sql.firstRow("select count(*) as count from user where id = ${id}").count > 0) {
            query = """\
                UPDATE \"USER\"
                SET cambio_password = 1,
                    cognome = ${cognome},
                    nome = ${nome},
                    password = ${userPassword}
                where id = ${id}
            """
        } else {
            query = """\
                INSERT INTO "USER" (ID,ACTIVE,CAMBIO_PASSWORD,COGNOME,NOME,PASSWORD,ROLE,USERNAME)
                VALUES (${id},1,1,${cognome},${nome},${userPassword},'USER',
                ${cognome.trim().replaceAll(' ', '-').toLowerCase() + '.' + nome.trim().replaceAll(' ', '-').toLowerCase()})
            """
        }
        if(SIMULATE){
            println "will execute: ${query}"
        }
        else {
            sql.execute(query)
        }
    }
    catch(all){
        println "Errore per [${line}]: ${all.message}"
    }
}
