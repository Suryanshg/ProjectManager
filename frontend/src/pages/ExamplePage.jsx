import ExampleComponent from "../components/ExampleComponent";
import { Component } from "react";
import { Link } from "react-router-dom";

class ExamplePage extends Component {
  componentDidMount() {
    console.log("Mounted");
  }
  callback(event) {
    console.log(event, "clicked");
  }
  render() {
    return (
      <div>
        <ExampleComponent callback={this.callback}>
          <div>Child</div>
          <Link to="/notfound">Not Found</Link>
        </ExampleComponent>
      </div>
    );
  }
}

export default ExamplePage;
