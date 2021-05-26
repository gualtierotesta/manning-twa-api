vcl 4.0;
backend default {
  .host = "api-catalog";
  .port = "6070";
}

sub vcl_backend_response {

  if (beresp.status == 200) {
    unset beresp.http.Cache-Control;
    set beresp.http.Cache-Control = "public; max-age=300";
    set beresp.ttl = 300s;
  }
  set beresp.http.Served-By = beresp.backend.name;
  set beresp.http.V-Cache-TTL = beresp.ttl;
  set beresp.http.V-Cache-Grace = beresp.grace;
}
