from collections.abc import Mapping
from werkzeug import Request, Response
from dify_plugin import Endpoint

class StaticFea(Endpoint):

    def _invoke(self, r: Request, values: Mapping, settings: Mapping) -> Response:
        """
        Invokes the endpoint with the given request.
        """
        # POST请求参数
        args = r.args
        def generator():
            yield args['fea_name']
        return Response(generator(), status=200, content_type="text/json")