FROM postgres:10
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD pass
ADD schema.sql /docker-entrypoint-initdb.d
EXPOSE 5432